async function loadSemesterOptions() {
    var response = await getMethod('/api/semester/public/all');
    if (response == null) {
        return;
    }
    var list = await response.json();
    var main = '<option value="">-- Chọn đợt thực tập --</option>';
    for (var i = 0; i < list.length; i++) {
        main += `<option value="${list[i].id}">Năm học: ${list[i].yearName}${list[i].isActive ? ' - đang mở' : ''}</option>`;
    }
    document.getElementById("semester").innerHTML = main;
}

async function loadSemesterTypeOptions(selectedId = null) {
    var semesterId = document.getElementById("semester").value;
    var select = document.getElementById("semesterTypeId");
    if (!semesterId) {
        select.innerHTML = '<option value="">-- Chọn loại thực tập --</option>';
        return;
    }
    var response = await getMethod(`/api/semester-type/public/find-all?semesterId=${semesterId}`);
    if (response == null) {
        return;
    }
    var list = await response.json();
    var main = '<option value="">-- Tất cả loại thực tập --</option>';
    for (var i = 0; i < list.length; i++) {
        main += `<option value="${list[i].id}">${list[i].type.displayName}</option>`;
    }
    select.innerHTML = main;
    if (selectedId) {
        select.value = selectedId;
    }
}

async function loadTeacherSelect() {
    var response = await getMethod('/api/user/admin/get-all-teacher');
    if (response == null) {
        return;
    }
    var list = await response.json();
    var main = '<option value="">-- Chọn giảng viên --</option>';
    for (var i = 0; i < list.length; i++) {
        main += `<option value="${list[i].id}">${list[i].fullname} - ${list[i].code || ''} - ${list[i].email}</option>`;
    }
    document.getElementById("teacherId").innerHTML = main;
    $('#teacherId').select2({
        theme: "bootstrap-5",
        width: '100%'
    });
}

async function loadSemesterTeachers() {
    var semesterId = document.getElementById("semester").value;
    var semesterTypeId = document.getElementById("semesterTypeId").value;
    var keyword = document.getElementById("keyword").value;

    var url = `/api/semester-teacher/admin/search?keyword=${encodeURIComponent(keyword || '')}`;
    if (semesterId) {
        url += `&semesterId=${semesterId}`;
    }
    if (semesterTypeId) {
        url += `&semesterTypeId=${semesterTypeId}`;
    }

    var response = await getMethod(url);
    if (response == null) {
        return;
    }
    var list = await response.json();
    if (list.length === 0) {
        document.getElementById("list-data").innerHTML = '<tr><td colspan="10" class="text-center">Không có dữ liệu</td></tr>';
        return;
    }

    var main = '';
    for (var i = 0; i < list.length; i++) {
        var item = list[i];
        main += `<tr id="data-row-${item.id}">
                    <td>${item.id}</td>
                    <td>${item.teacher.fullname}</td>
                    <td>${item.teacher.code || ''}</td>
                    <td>${item.semesterType.semester.yearName}</td>
                    <td>${item.semesterType.type.displayName}</td>
                    <td>${item.projectName || ''}</td>
                    <td>${item.maxStudents || 0}</td>
                    <td>${item.currentStudents || 0}</td>
                    <td>${item.semesterType.deadlineRegis || ''}</td>
                    <td class="text-end">
                        <button onclick="showSemesterTeacherDetail(${item.id})" class="btn btn-sm btn-outline-info"><i class="bi bi-eye"></i></button>
                        <a href="add-semester-teacher?id=${item.id}" class="btn btn-sm btn-warning"><i class="bi bi-pencil"></i></a>
                        <button onclick="deleteSemesterTeacher(${item.id})" class="btn btn-sm btn-danger"><i class="bi bi-trash"></i></button>
                    </td>
                </tr>`;
    }
    document.getElementById("list-data").innerHTML = main;
}

async function loadASemesterTeacher() {
    var params = new URLSearchParams(window.location.search);
    var id = params.get("id");
    if (!id) {
        return;
    }
    var response = await getMethod(`/api/semester-teacher/admin/${id}`);
    if (response == null) {
        return;
    }
    var result = await response.json();
    document.getElementById("semester").value = result.semesterType.semester.id;
    await loadSemesterTypeOptions(result.semesterType.id);
    document.getElementById("semesterTypeId").value = result.semesterType.id;
    $("#teacherId").val(String(result.teacher.id)).trigger('change');
    document.getElementById("maxStudents").value = result.maxStudents || "";
    document.getElementById("projectName").value = result.projectName || "";
    document.getElementById("descriptionProject").value = result.descriptionProject || "";
}

async function saveSemesterTeacher() {
    var params = new URLSearchParams(window.location.search);
    var id = params.get("id");
    var payload = {
        id: id,
        semesterTypeId: document.getElementById("semesterTypeId").value,
        teacherId: document.getElementById("teacherId").value,
        maxStudents: document.getElementById("maxStudents").value,
        projectName: document.getElementById("projectName").value,
        descriptionProject: document.getElementById("descriptionProject").value
    };

    if (!payload.semesterTypeId) {
        toastr.error("Vui lòng chọn loại thực tập");
        return;
    }
    if (!payload.teacherId) {
        toastr.error("Vui lòng chọn giảng viên");
        return;
    }
    if (!payload.maxStudents || Number(payload.maxStudents) <= 0) {
        toastr.error("Số lượng sinh viên tối đa phải lớn hơn 0");
        return;
    }
    if (!payload.projectName || payload.projectName.trim() === "") {
        toastr.error("Tên đề tài không được bỏ trống");
        return;
    }

    var response = await postMethodPayload('/api/semester-teacher/admin/create-update', payload);
    if (response != null) {
        swal({
            title: "Thông báo",
            text: id ? "Cập nhật giảng viên hướng dẫn thành công" : "Thêm giảng viên hướng dẫn thành công",
            type: "success"
        }, function() {
            window.location.href = 'semester-teacher';
        });
    }
}

async function deleteSemesterTeacher(id) {
    swal({
        title: "Bạn có chắc chắn muốn xóa?",
        text: "Hành động này không thể hoàn tác!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Xóa",
        cancelButtonText: "Hủy",
        closeOnConfirm: false
    }, async function(isConfirm) {
        if (isConfirm) {
            var response = await deleteMethod(`/api/semester-teacher/admin/delete?id=${id}`);
            if (response) {
                swal("Đã xóa!", "Giảng viên hướng dẫn đã được xóa khỏi hệ thống.", "success");
                var row = document.getElementById(`data-row-${id}`);
                if (row) {
                    row.remove();
                }
            }
        }
    });
}

async function showSemesterTeacherDetail(id) {
    var response = await getMethod(`/api/semester-teacher/admin/detail/${id}`);
    if (response == null) {
        return;
    }

    var result = await response.json();
    document.getElementById("detail-project-name").textContent = result.projectName || "Chưa có tên đề tài";
    document.getElementById("detail-teacher-name").textContent = result.teacherName || "";
    document.getElementById("detail-teacher-code").textContent = result.teacherCode || "";
    document.getElementById("detail-teacher-email").textContent = result.teacherEmail || "";
    document.getElementById("detail-teacher-phone").textContent = result.teacherPhone || "";
    document.getElementById("detail-semester-name").textContent = result.semesterName || "";
    document.getElementById("detail-internship-type").innerHTML = renderBadge(result.internshipType);
    document.getElementById("detail-deadline").textContent = formatDateTime(result.deadlineRegis);
    document.getElementById("detail-max-students").textContent = result.maxStudents ?? 0;
    document.getElementById("detail-current-students").textContent = result.currentStudents ?? 0;
    document.getElementById("detail-remaining-students").textContent = result.remainingStudents ?? 0;
    document.getElementById("detail-total-registrations").textContent = result.totalRegistrations ?? 0;
    document.getElementById("detail-description-project").textContent = result.descriptionProject || "Chưa có mô tả đề tài";
    document.getElementById("detail-edit-link").href = `add-semester-teacher?id=${result.id}`;

    var students = result.students || [];
    document.getElementById("detail-student-count").textContent = `${students.length} sinh viên`;
    renderSemesterTeacherStudents(students);

    var modalElement = document.getElementById("semesterTeacherDetailModal");
    bootstrap.Modal.getOrCreateInstance(modalElement).show();
}

function renderSemesterTeacherStudents(list) {
    if (!list || list.length === 0) {
        document.getElementById("detail-student-list").innerHTML = '<tr><td colspan="9" class="text-center text-muted">Chưa có sinh viên nào đăng ký</td></tr>';
        return;
    }

    var main = '';
    for (var i = 0; i < list.length; i++) {
        var item = list[i];
        main += `<tr>
                    <td>${i + 1}</td>
                    <td>${item.studentName || ''}</td>
                    <td>${item.studentCode || ''}</td>
                    <td>${item.className || ''}</td>
                    <td>${renderBadge(item.internshipType)}</td>
                    <td>${item.companyName || ''}</td>
                    <td>${renderBadge(item.status)}</td>
                    <td>${formatDateTime(item.registerDate)}</td>
                    <td>${formatScore(item.totalScore)}</td>
                </tr>`;
    }
    document.getElementById("detail-student-list").innerHTML = main;
}

function renderBadge(item) {
    if (!item) {
        return '';
    }
    return `<span class="badge" style="background:${item.color};color:#fff">${item.displayName}</span>`;
}

function formatDateTime(value) {
    if (!value) {
        return '';
    }
    return new Date(value).toLocaleString('vi-VN');
}

function formatScore(value) {
    if (value === null || value === undefined) {
        return '0.0';
    }
    return Number(value).toFixed(1);
}
