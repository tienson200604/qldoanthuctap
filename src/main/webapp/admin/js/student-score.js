var token = localStorage.getItem("token");

// ── State lọc hiện tại ──
var currentSemesterId = null;
var currentTeacherId = null;
var currentKeyword = null;
var currentClassName = null;

// ── Load dropdowns ──
async function loadSemesters() {
    var response = await getMethod(`/api/semester/public/all`);
    var list = await response.json();
    var opts = '';
    for (var i = 0; i < list.length; i++) {
        opts += `<option value="${list[i].id}">Năm học: ${list[i].yearName}</option>`;
    }
    document.getElementById("filterSemester").innerHTML = opts;
    $('#filterSemester').select2({ theme: "bootstrap-5", width: '100%' });

    // Tự load lần đầu
    currentSemesterId = list[0]?.id;
    if (currentSemesterId) loadScores();
}

async function loadTeachers() {
    var response = await getMethod(`/api/user/admin/get-all-teacher`);
    if (response == null) return;
    var list = await response.json();
    var opts = '<option value="">-- Tất cả giáo viên --</option>';
    for (var i = 0; i < list.length; i++) {
        opts += `<option value="${list[i].id}">${list[i].fullname}</option>`;
    }
    document.getElementById("filterTeacher").innerHTML = opts;
    $('#filterTeacher').select2({ theme: "bootstrap-5", width: '100%' });
}

// ── Load bảng điểm ──
async function loadScores() {
    currentSemesterId = document.getElementById("filterSemester").value;
    currentTeacherId  = document.getElementById("filterTeacher").value  || null;
    currentKeyword    = document.getElementById("filterKeyword").value  || null;
    currentClassName  = document.getElementById("filterClass").value    || null;

    if (!currentSemesterId) return;

    var url = `/api/score_componentApi/admin/student-scores?semesterId=${currentSemesterId}`;
    if (currentTeacherId) url += `&teacherId=${currentTeacherId}`;
    if (currentKeyword)   url += `&keyword=${encodeURIComponent(currentKeyword)}`;
    if (currentClassName) url += `&className=${encodeURIComponent(currentClassName)}`;

    if ($.fn.DataTable.isDataTable('#scoreTable')) {
        $('#scoreTable').DataTable().destroy();
    }

    var response = await getMethod(url);
    if (response == null) {
        document.getElementById("scoreTableBody").innerHTML =
            '<tr><td colspan="10" class="text-center">Lỗi khi tải dữ liệu</td></tr>';
        return;
    }
    var list = await response.json();
    var html = '';
    if (list.length === 0) {
        html = '<tr><td colspan="10" class="text-center text-muted">Không có dữ liệu</td></tr>';
    } else {
        for (var i = 0; i < list.length; i++) {
            var s = list[i];
            var score = s.totalScore != null ? parseFloat(s.totalScore).toFixed(2) : '—';
            var badge = getScoreBadge(s.totalScore);
            html += `
            <tr>
                <td>${i + 1}</td>
                <td>${s.studentCode || ''}</td>
                <td>${s.studentName || ''}</td>
                <td>${s.className || ''}</td>
                <td>${s.semesterYear || ''}</td>
                <td>${s.teacherName || ''}</td>
                <td>${formatInternshipType(s.internshipType)}</td>
                <td class="fw-bold">${score}</td>
                <td><span class="badge ${badge.cls}">${s.rate || '—'}</span></td>
                <td>
                    <button class="btn btn-sm btn-outline-info" onclick="showDetail(${s.studentRegisId})">
                        <i class="bi bi-eye"></i> Chi tiết
                    </button>
                </td>
            </tr>`;
        }
    }
    document.getElementById("scoreTableBody").innerHTML = html;
    $('#scoreTable').DataTable({
        language: { url: '//cdn.datatables.net/plug-ins/1.13.7/i18n/vi.json' },
        pageLength: 15,
        order: [[7, 'desc']],
        errMode: 'none'
    });
    $.fn.dataTable.ext.errMode = 'none';
}

function getScoreBadge(score) {
    if (score == null)  return { cls: 'bg-secondary' };
    if (score >= 8.5)   return { cls: 'bg-success' };
    if (score >= 7.0)   return { cls: 'bg-primary' };
    if (score >= 5.5)   return { cls: 'bg-warning text-dark' };
    return { cls: 'bg-danger' };
}

function formatInternshipType(type) {
    if (!type) return '—';
    const map = {
        'TAI_TRUONG':            'Tại trường',
        'DOANH_NGHIEP_LIEN_KET': 'Công ty liên kết',
        'DOANH_NGHIEP_NGOAI':    'Doanh nghiệp ngoài'
    };
    return map[type] || type;
}

// ── Xem chi tiết ──
async function showDetail(studentRegisId) {
    var response = await getMethod(`/api/score_componentApi/admin/find-by-studentRegis?studentRegisId=${studentRegisId}`);
    if (response == null) return;
    var data = await response.json();

    var scored    = data.scored    || [];
    var notScored = data.notScored || [];

    var html = '';
    if (scored.length === 0 && notScored.length === 0) {
        html = '<tr><td colspan="4" class="text-center text-muted">Chưa có đầu điểm nào</td></tr>';
    } else {
        for (var i = 0; i < scored.length; i++) {
            var sc = scored[i];
            var weighted = (sc.point * sc.percent / 100).toFixed(2);
            html += `<tr>
                <td>${sc.name}</td>
                <td>${sc.percent}%</td>
                <td class="fw-bold">${sc.point}</td>
                <td>${weighted}</td>
            </tr>`;
        }
        for (var j = 0; j < notScored.length; j++) {
            html += `<tr class="text-muted">
                <td>${notScored[j].name}</td>
                <td>${notScored[j].percent}%</td>
                <td>—</td>
                <td>—</td>
            </tr>`;
        }
    }
    document.getElementById("detailTableBody").innerHTML = html;
    var modal = new bootstrap.Modal(document.getElementById('detailModal'));
    modal.show();
}

// ── Xuất Excel ──
async function exportExcel() {
    if (!currentSemesterId) { alert('Vui lòng chọn học kỳ trước!'); return; }
    var url = `/api/score_componentApi/admin/export-excel?semesterId=${currentSemesterId}`;
    if (currentTeacherId) url += `&teacherId=${currentTeacherId}`;
    if (currentKeyword)   url += `&keyword=${encodeURIComponent(currentKeyword)}`;
    if (currentClassName) url += `&className=${encodeURIComponent(currentClassName)}`;

    var link = document.createElement('a');
    link.href = url;
    link.download = 'diem-sinh-vien.xlsx';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

window.onload = async function () {
    await loadSemesters();
    loadTeachers();
};
