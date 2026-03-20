var token = localStorage.getItem("token");
var size = 10;
async function loadBlog(page) {
    var response = await getMethod(`/api/blog/public/findAll?page=${page}&size=${size}`)
    if(response == null){
        document.getElementById("list-data").innerHTML = '<tr><td colspan="8" class="text-center">Lỗi khi tải dữ liệu</td></tr>';
    }
    var result = await response.json();
    var totalPage = result.totalPages
    var list = result.content;
    if(list.length == 0){
        document.getElementById("list-data").innerHTML = '<tr><td colspan="8" class="text-center">Không có dữ liệu</td></tr>';
        return;
    }
    var main = '';
    for (i = 0; i < list.length; i++) {
        main += ` <tr id="data-row-${list[i].id}">
                    <td><img src="${list[i].image}" style="width:40px"></td>
                    <td>${list[i].id}</td>
                    <td>${list[i].title}</td>
                    <td>${list[i].category.name}</td>
                    <td>${list[i].createdTime} ${list[i].createdDate}</td>
                    <td>${list[i].description}</td>
                    <td>${list[i].numView}</td>
                    <td>${list[i].user.email}</td>
                    <td class="sticky-col">
                        <button onclick="deleteBlog(${list[i].id})" class="btn btn-sm btn-danger"><i class="bi bi-trash"></i></button>
                        <a href="add-blog?id=${list[i].id}" class="btn btn-sm btn-primary"><i class="bi bi-pencil"></i></a>
                    </td>
                </tr>`
    }
    document.getElementById("list-data").innerHTML = main
    var mainpage = ''
    for (i = 1; i <= totalPage; i++) {
        mainpage += `<li onclick="loadBlog(${(Number(i) - 1)})" class="page-item ${Number(i)-1 == page?'active':''}"><a class="page-link" href="#">${i}</a></li>`
    }
    document.getElementById("pagination-work").innerHTML = mainpage
}

async function loadCategorySelect() {
    var response = await getMethod(`/api/category/public/all-by-type?type=BLOG`)
    var list = await response.json();
    var main = '';
    for (i = 0; i < list.length; i++) {
        main += `<option value="${list[i].id}">${list[i].name}</option>`
    }
    document.getElementById("category").innerHTML = main
    $('#category').select2({
        theme: "bootstrap-5",
        width: '100%', 
        templateSelection: (data) => data.text 
    });
}


async function saveBlog() {
    var uls = new URL(document.URL)
    var id = uls.searchParams.get("id");
    var payload = {
        "id": id,
        "title": document.getElementById("title").value,
        "description": document.getElementById("description").value,
        "content": tinyMCE.get('editor').getContent(),
        "image": document.getElementById("image").value,
        "categoryId": document.getElementById("category").value,
    }
    var response = await postMethodPayload(`/api/blog/admin/create-update`, payload)
    if (response != null) {
        swal({
            title: "Thông báo",
            text: id==null?"thêm tin tức thành công!":"Cập nhật tin tức thành công",
            type: "success"
        },
        function() {
            window.location.replace('blog');
        });
    }
}

async function loadABlog() {
    var id = window.location.search.split('=')[1];
    if (id != null) {
        var response = await getMethod(`/api/blog/public/findById?id=${id}`)
        if(response == null){
            return;
        }
        var result = await response.json();
        document.getElementById("title").value = result.title
        document.getElementById("description").value = result.description
        $("#category").val(result.category.id).change()
        document.getElementById("image").value = result.image
        if(result.image){
            document.getElementById("imgpreview").src = result.image
        }
         tinyMCE.get('editor').setContent(result.content)
    }
}

async function deleteBlog(id) {
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
            var response = await deleteMethod(`/api/blog/admin/delete?id=${id}`)
            if (response) {
                swal("Đã xóa!", "Tin tức đã được xóa khỏi hệ thống.", "success");
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
    document.getElementById("btn-submit").innerText = "Lưu tin tức"
}
