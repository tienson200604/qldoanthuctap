var token = localStorage.getItem("token");
var size = 10;
async function loadDiem() {
    $('#example').DataTable().destroy();
    var namhoc = document.getElementById("semester").value
    var response = await getMethod(`/api/score-ratio/public/find-by-semesterId?semesterId=${namhoc}`)
    if(response == null){
        document.getElementById("list-data").innerHTML = '<tr><td colspan="5" class="text-center">Lỗi khi tải dữ liệu</td></tr>';
    }
    var list = await response.json();
    // if(list.length == 0){
    //     document.getElementById("list-data").innerHTML = '<tr><td colspan="5" class="text-center">Không có dữ liệu</td></tr>';
    //     return;
    // }
    var main = '';
    for (i = 0; i < list.length; i++) {
        main += ` <tr id="data-row-${list[i].id}">
                    <td>${list[i].id}</td>
                    <td>${list[i].name}</td>
                    <td>${list[i].percent} %</td>
                    <td>${list[i].semester.yearName}</td>
                    <td class="sticky-col">
                        <button onclick="deleteDiem(${list[i].id})" class="btn btn-sm btn-danger"><i class="bi bi-trash"></i></button>
                        <a href="add-score?id=${list[i].id}" class="btn btn-sm btn-primary"><i class="bi bi-pencil"></i></a>
                    </td>
                </tr>`
    }
    document.getElementById("list-data").innerHTML = main
    $('#example').DataTable({
        language: {
            url: '//cdn.datatables.net/plug-ins/1.13.7/i18n/vi.json'
        }
    });
}

async function loadNamHoc() {
    var response = await getMethod(`/api/semester/public/all`)
    var list = await response.json();
    var main = '';
    for (i = 0; i < list.length; i++) {
        main += `<option value="${list[i].id}">Năm học: ${list[i].yearName}</option>`
    }
    document.getElementById("semester").innerHTML = main
    $('#semester').select2({
        theme: "bootstrap-5",
        width: '100%', 
        templateSelection: (data) => data.text 
    });
}


async function saveDiem() {
    var uls = new URL(document.URL)
    var id = uls.searchParams.get("id");
    var payload = {
        "id": id,
        "name": document.getElementById("name").value,
        "percent": document.getElementById("percent").value,
        "semesterId": document.getElementById("semester").value,
    }
    var response = await postMethodPayload(`/api/score-ratio/admin/create-update`, payload)
    if (response != null) {
        swal({
            title: "Thông báo",
            text: id==null?"thêm đầu điểm thành công!":"Cập nhật đầu điểm thành công",
            type: "success"
        },
        function() {
            window.location.replace('score');
        });
    }
}

async function loadADiem() {
    var id = window.location.search.split('=')[1];
    if (id != null) {
        var response = await getMethod(`/api/score-ratio/public/find-by-id?id=${id}`)
        if(response == null){
            return;
        }
        var result = await response.json();
        document.getElementById("name").value = result.name
        document.getElementById("percent").value = result.percent
        $("#semester").val(result.semester.id).change()
    }
}

async function deleteDiem(id) {
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
            var response = await deleteMethod(`/api/score-ratio/admin/delete?id=${id}`)
            if (response) {
                swal("Đã xóa!", "Đầu điểm đã được xóa khỏi hệ thống.", "success");
                document.getElementById(`data-row-${id}`).remove();
            }
        }
    });
}

