var token = localStorage.getItem("token");
var size = 10;
async function loadNamHoc() {
    $('#example').DataTable().destroy();
    var response = await getMethod(`/api/semester/public/all`)
    if(response == null){
        document.getElementById("list-data").innerHTML = '<tr><td colspan="5" class="text-center">Lỗi khi tải dữ liệu</td></tr>';
    }
    var list = await response.json();
    if(list.length == 0){
        document.getElementById("list-data").innerHTML = '<tr><td colspan="5" class="text-center">Không có dữ liệu</td></tr>';
        return;
    }
    var main = '';
    for (i = 0; i < list.length; i++) {
        main += ` <tr id="data-row-${list[i].id}">
                    <td>${list[i].id}</td>
                    <td>${list[i].yearName}</td>
                    <td>${list[i].startDate}</td>
                    <td>${list[i].endDate}</td>
                    <td>${list[i].isActive == true?'<span class="badge bg-success">Đang diễn ra</span>':'<span class="badge bg-secondary">Đã kết thúc</span>'}</td>
                    <td class="sticky-col">
                        <button onclick="deleteNamHoc(${list[i].id})" class="btn btn-sm btn-danger"><i class="bi bi-trash"></i></button>
                        <a href="add-semester?id=${list[i].id}" class="btn btn-sm btn-primary"><i class="bi bi-pencil"></i></a>
                    </td>
                </tr>`
    }
    document.getElementById("list-data").innerHTML = main
    $('#example').DataTable({
    "order": [[4, "asc"]], // Sắp xếp cột thứ 5 (index 4) theo thứ tự giảm dần
    "language": {
        "url": "//cdn.datatables.net/plug-ins/1.13.4/i18n/vi.json" // Thêm tiếng Việt cho chuyên nghiệp
    }
});
}

async function saveNamHoc() {
    var uls = new URL(document.URL)
    var id = uls.searchParams.get("id");
    var payload = {
        "id": id,
        "yearName": document.getElementById("yearName").value,
        "startDate": document.getElementById("startDate").value,
        "endDate": document.getElementById("endDate").value,
        "isActive": document.getElementById("isActive").checked,
    }
    var response = await postMethodPayload(`/api/semester/admin/create-update`, payload)
    if (response != null) {
        swal({
            title: "Thông báo",
            text: id==null?"thêm năm học thành công!":"Cập nhật năm học thành công",
            type: "success"
        },
        function() {
            window.location.replace('semester');
        });
    }
}

async function loadANamHoc() {
    var id = window.location.search.split('=')[1];
    if (id != null) {
        var response = await getMethod(`/api/semester/public/find-by-id?id=${id}`)
        if(response == null){
            return;
        }
        var result = await response.json();
        document.getElementById("yearName").value = result.yearName
        document.getElementById("startDate").value = result.startDate
        document.getElementById("endDate").value = result.endDate
        document.getElementById("isActive").checked = result.isActive
    }
}

async function deleteNamHoc(id) {
    swal({
        title: "Bạn có chắc chắn muốn xóa?",
        text: "Hành động này không thể hoàn tác!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Xóa",
        cancelButtonText: "Hủy",
        closeOnConfirm: false 
    },
    async function(isConfirm) {
        if (isConfirm) {
            var response = await deleteMethod(`/api/semester/admin/delete?id=${id}`)
            if (response) {
                swal("Đã xóa!", "Năm học đã được xóa khỏi hệ thống.", "success");
                document.getElementById(`data-row-${id}`).remove();
            }
        }
    });
}

