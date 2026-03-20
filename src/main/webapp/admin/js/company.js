var token = localStorage.getItem("token");
var size = 10;
async function loadCompany(page) {
    var url = `/api/company/public/find-all?page=${page}&size=${size}`;
    var search = document.getElementById("search").value;
    if(search != null && search != ''){
        url += `&search=${search}`
    }
    var response = await getMethod(url)
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
    for (let i = 0; i < list.length; i++) {
    let statusBadge = list[i].active 
        ? '<span class="badge bg-success">Hoạt động</span>' 
        : '<span class="badge bg-secondary">Ngừng hợp tác</span>';
        main += `
            <tr id="data-row-${list[i].id}">
                <td>
                    <img src="${list[i].imageBanner || 'default-company.png'}" 
                        style="width:70px; height:40px; object-fit:cover" class="rounded border">
                </td>
                <td>${list[i].id}</td>
                <td>
                    <div class="fw-bold">${list[i].name}</div>
                    <a class="text-primary" href="${list[i].website}" target="_blank">${list[i].website || ''}</small>
                </td>
                <td>
                    <div style="font-size: 0.85rem;">
                        <i class="bi bi-envelope"></i> ${list[i].email}<br>
                        <i class="bi bi-telephone"></i> ${list[i].phone}
                    </div>
                </td>
                <td>${list[i].address}</td>
                <td><code class="text-dark">${list[i].taxCode || 'N/A'}</code></td>
                <td>${statusBadge}</td>
                <td class="sticky-col">
                    <div class="d-flex gap-1">
                        <button onclick="deleteCompany(${list[i].id})" class="btn btn-sm btn-danger">
                            <i class="bi bi-trash"></i>
                        </button>
                        <a href="add-company?id=${list[i].id}" class="btn btn-sm btn-primary">
                            <i class="bi bi-pencil"></i>
                        </a>
                    </div>
                </td>
            </tr>`;
    }
    document.getElementById("list-data").innerHTML = main
    var mainpage = ''
    for (i = 1; i <= totalPage; i++) {
        mainpage += `<li onclick="loadBlog(${(Number(i) - 1)})" class="page-item ${Number(i)-1 == page?'active':''}"><a class="page-link" href="#">${i}</a></li>`
    }
    document.getElementById("pagination").innerHTML = mainpage
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


async function saveCompany() {
    var uls = new URL(document.URL)
    var id = uls.searchParams.get("id");
    var payload = {
        "id": id,
        "name": document.getElementById("name").value,
        "address": document.getElementById("address").value,
        "description": tinyMCE.get('editor').getContent(),
        "imageBanner": document.getElementById("image").value,
        "phone": document.getElementById("phone").value,
        "email": document.getElementById("email").value,
        "website": document.getElementById("website").value,
        "taxCode": document.getElementById("taxCode").value,
        "active": document.getElementById("active").checked,
    }
    var response = await postMethodPayload(`/api/company/admin/create-update`, payload)
    if (response != null) {
        swal({
            title: "Thông báo",
            text: id==null?"thêm công ty liên kết thành công!":"Cập nhật công ty liên kết thành công",
            type: "success"
        },
        function() {
            window.location.replace('company');
        });
    }
}

async function loadACompany() {
    var id = window.location.search.split('=')[1];
    if (id != null) {
        var response = await getMethod(`/api/company/public/find-by-id?id=${id}`)
        if(response == null){
            return;
        }
        var result = await response.json();
        document.getElementById("name").value = result.name
        document.getElementById("address").value = result.address
        document.getElementById("phone").value = result.phone
        document.getElementById("email").value = result.email
        document.getElementById("website").value = result.website
        document.getElementById("taxCode").value = result.taxCode
        document.getElementById("active").checked = result.active
        document.getElementById("image").value = result.imageBanner
        if(result.imageBanner){
            document.getElementById("imgpreview").src = result.imageBanner
        }
         tinyMCE.get('editor').setContent(result.description)
    }
}

async function deleteCompany(id) {
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
            var response = await deleteMethod(`/api/company/admin/delete?id=${id}`)
            if (response) {
                swal("Đã xóa!", "Công ty liên kết đã được xóa khỏi hệ thống.", "success");
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
    document.getElementById("btn-submit").innerText = "Lưu công ty"
}
