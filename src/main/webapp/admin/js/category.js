var token = localStorage.getItem("token");
var size = 10;
async function loadCategory() {
    $('#example').DataTable().destroy();
    var response = await getMethod(`/api/category/public/all`)
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
                    <td><img src="${list[i].image}" style="width:40px"></td>
                    <td>${list[i].id}</td>
                    <td>${list[i].name}</td>
                    <td>${list[i].categoryType}</td>
                    <td class="sticky-col">
                        <button onclick="deleteCategory(${list[i].id})" class="btn btn-sm btn-danger"><i class="bi bi-trash"></i></button>
                        <a href="add-category?id=${list[i].id}" class="btn btn-sm btn-primary"><i class="bi bi-pencil"></i></a>
                    </td>
                </tr>`
    }
    document.getElementById("list-data").innerHTML = main
     $('#example').DataTable();
}


async function saveCategory() {
    var uls = new URL(document.URL)
    var id = uls.searchParams.get("id");
    var payload = {
        "id": id,
        "name": document.getElementById("catename").value,
        "image": document.getElementById("image").value,
        "categoryType": document.getElementById("categoryType").value,
    }
    var response = await postMethodPayload(`/api/category/admin/create-update`, payload)
    if (response != null) {
        swal({
            title: "Thông báo",
            text: id==null?"thêm danh mục thành công!":"Cập nhật danh mục thành công",
            type: "success"
        },
        function() {
            window.location.replace('category');
        });
    }
}

async function loadACategory() {
    var id = window.location.search.split('=')[1];
    if (id != null) {
        var response = await getMethod(`/api/category/public/find-by-id?id=${id}`)
        if(response == null){
            return;
        }
        var result = await response.json();
        document.getElementById("catename").value = result.name
        document.getElementById("categoryType").value = result.categoryType
        document.getElementById("image").value = result.image
        if(result.image){
            document.getElementById("imgpreview").src = result.image
        }
    }
}

async function deleteCategory(id) {
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
            var response = await deleteMethod(`/api/category/admin/delete?id=${id}`)
            if (response) {
                swal("Đã xóa!", "Danh mục đã được xóa khỏi hệ thống.", "success");
                document.getElementById(`data-row-${id}`).remove();
            }
        }
    });
}

async function chooseAvatar(){
    document.getElementById("btn-submit").disabled = true
    document.getElementById("btn-submit").innerText = "Đang tải ảnh..."
    const filePath = document.getElementById('fileanhdaidientl')
    const formData = new FormData()
    formData.append("file", filePath.files[0])
    var urlUpload = 'http://localhost:8080/api/public/upload-file';
    const res = await fetch(urlUpload, {
        method: 'POST',
        body: formData
    });
    if (res.status < 300) {
        const linkImage = await res.text();
        document.getElementById("image").value = linkImage;
        document.getElementById("imgpreview").src = linkImage
    }
    document.getElementById("btn-submit").disabled = false
    document.getElementById("btn-submit").innerText = "Lưu danh mục"
}
