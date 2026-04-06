var token = localStorage.getItem("token");
var size = 10;

document.addEventListener("DOMContentLoaded", function() {
    loadSemesters();
    loadInternStudents(0);
});

async function loadSemesters() {
    const response = await fetch('http://localhost:8080/api/semester/public/all');
    const semesters = await response.json();
    let options = '<option value="">Tất cả đợt thực tập</option>';
    for (let i = 0; i < semesters.length; i++) {
        options += `<option value="${semesters[i].id}">${semesters[i].yearName}</option>`;
    }
    document.getElementById("semesterId").innerHTML = options;
}

async function loadInternStudents(page) {
    var keyword = document.getElementById("search").value.trim();
    var semesterId = document.getElementById("semesterId").value;
    var internshipType = document.getElementById("internshipType").value;
    var status = document.getElementById("status").value;

    var url = 'http://localhost:8080/api/student-regis/admin/search?page=' + page + '&size=' + size;
    if (keyword !== '') url += '&keyword=' + encodeURIComponent(keyword);
    if (semesterId !== '') url += '&semesterId=' + semesterId;
    if (internshipType !== '') url += '&internshipType=' + internshipType;
    if (status !== '') url += '&status=' + status;

    const response = await fetch(url, {
        method: 'GET',
        headers: new Headers({
            'Authorization': 'Bearer ' + token
        })
    });
    var result = await response.json();
    renderInternStudents(result.content || [], page, result.totalPages || 0);
}

function renderInternStudents(list, page, totalPage) {
    if (list.length === 0) {
        document.getElementById("list-intern-students").innerHTML = '<tr><td colspan="11" class="text-center">Không có dữ liệu</td></tr>';
        document.getElementById("pagination").innerHTML = '';
        return;
    }

    var main = '';
    for (let i = 0; i < list.length; i++) {
        main += `<tr>
                    <td>${page * size + i + 1}</td>
                    <td>${list[i].studentName || ''}</td>
                    <td>${list[i].studentCode || ''}</td>
                    <td>${list[i].className || ''}</td>
                    <td>${list[i].semesterName || ''}</td>
                    <td>${formatInternshipType(list[i].internshipType)}</td>
                    <td>${list[i].teacherName || ''}</td>
                    <td>${list[i].companyName || ''}</td>
                    <td>${formatStatus(list[i].status)}</td>
                    <td>${formatDateTime(list[i].registerDate)}</td>
                    <td>${renderActionButton(list[i])}</td>
                </tr>`;
    }
    document.getElementById("list-intern-students").innerHTML = main;
    pageable(page, totalPage, "loadInternStudents");
}

function formatInternshipType(type) {
    if (!type) return '';
    return `<span class="badge" style="background:${type.color};color:#fff">${type.displayName}</span>`;
}

function formatStatus(status) {
    if (!status) return '';
    return `<span class="badge" style="background:${status.color};color:#fff">${status.displayName}</span>`;
}

function formatDateTime(value) {
    if (!value) return '';
    return new Date(value).toLocaleString('vi-VN');
}

function renderActionButton(item) {
    if (item.status && (item.status.name === 'DANG_THUC_HIEN' || item.status.name === 'CANH_CAO')) {
        return `<button class="btn btn-sm btn-outline-success" onclick="completeInternStudent(${item.id})">Hoàn thành</button>`;
    }
    return '';
}

async function completeInternStudent(id) {
    swal({
        title: "Xác nhận",
        text: "Bạn có chắc chắn muốn đánh dấu sinh viên này đã hoàn thành thực tập?",
        type: "success",
        showCancelButton: true,
        confirmButtonColor: "#28a745",
        confirmButtonText: "Hoàn thành",
        cancelButtonText: "Hủy",
        closeOnConfirm: false
    }, async function() {
        const response = await fetch(`http://localhost:8080/api/student-regis/admin/complete?id=${id}`, {
            method: 'POST',
            headers: new Headers({
                'Authorization': 'Bearer ' + token
            })
        });
        if (response.status < 300) {
            swal("Thông báo", "Đã cập nhật trạng thái hoàn thành", "success");
            loadInternStudents(0);
            return;
        }
        if (response.status === exceptionCode) {
            const result = await response.json();
            swal("Thông báo", result.defaultMessage, "error");
            return;
        }
        swal("Thông báo", "Cập nhật trạng thái thất bại", "error");
    });
}

function exportInternStudentsExcel() {
    var keyword = document.getElementById("search").value.trim();
    var semesterId = document.getElementById("semesterId").value;
    var internshipType = document.getElementById("internshipType").value;
    var status = document.getElementById("status").value;

    var url = 'http://localhost:8080/api/student-regis/admin/export-excel';
    var params = [];
    if (keyword !== '') params.push('keyword=' + encodeURIComponent(keyword));
    if (semesterId !== '') params.push('semesterId=' + semesterId);
    if (internshipType !== '') params.push('internshipType=' + internshipType);
    if (status !== '') params.push('status=' + status);
    if (params.length > 0) {
        url += '?' + params.join('&');
    }

    fetch(url, {
        method: 'GET',
        headers: new Headers({
            'Authorization': 'Bearer ' + token
        })
    })
        .then(response => response.blob())
        .then(blob => {
            const link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = 'danh-sach-sinh-vien-thuc-tap.xlsx';
            link.click();
        });
}
