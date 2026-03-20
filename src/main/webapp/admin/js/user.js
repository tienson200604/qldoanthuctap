var token = localStorage.getItem("token");
var size = 10;


async function loadUsers(page) {
    var search = document.getElementById("search").value
    var status = document.getElementById("status").value
    var role = document.getElementById("roles").value
    var url = 'http://localhost:8080/api/user/admin/search?page=' + page + '&size=' + size + '&keyword=' + search;
    if (status) {
        url += '&actived=' + status;
    }
    if (role) {
        url += '&authority=' + role;
    }
    const response = await fetch(url, {
        method: 'GET',
        headers: new Headers({
            'Authorization': 'Bearer ' + token,
        }),
    });
    var result = await response.json();
    console.log(result)
    var list = result.content;
    var totalPage = result.totalPages;
    if(list.length == 0){
        document.getElementById("list-user").innerHTML = '<tr><td colspan="5" class="text-center">Không có dữ liệu</td></tr>';
        document.getElementById("pagination").innerHTML = '';
        return;
    }
    var main = '';
    for (i = 0; i < list.length; i++) {
        main += ` <tr id="user-row-${list[i].id}">
                    <td>${page * size + Number(i) + Number(1)}</td>
                    <td><img src="${list[i].avatar || '/image/default-avatar.jpg'}" class="rounded-circle" width="35"></td>
                    <td>${list[i].id}</td>
                    <td>${list[i].fullname}</td>
                    <td>${list[i].code}</td>
                    <td>${list[i].email}</td>
                    <td>${list[i].phone == null?'':list[i].phone}</td>
                    <td>${list[i].authorities.name}</td>
                    <td id="status-${list[i].id}"><span class="badge bg-${list[i].actived === true ? 'success' : 'danger'}">${list[i].actived === true ? 'Đang hoạt động' : 'Đã khóa'}</span></td>
                    <td class="text-end">
                        <a href="add-user?id=${list[i].id}" class="btn btn-sm btn-warning"><i class="bi bi-pencil"></i></a>
                        ${list[i].actived === true 
                            ? `<button id="toggle-${list[i].id}" onclick="toggleUserStatus(${list[i].id}, 1)" class="btn btn-sm btn-danger"><i class="bi bi-lock"></i></button>`
                            : `<button id="toggle-${list[i].id}" onclick="toggleUserStatus(${list[i].id}, 0)" class="btn btn-sm btn-success"><i class="bi bi-unlock"></i></button>`
                        }
                        <button onclick="deleteUser(${list[i].id})" class="btn btn-sm btn-danger"><i class="bi bi-trash"></i></button>
                    </td>
                </tr>`
    }
    document.getElementById("list-user").innerHTML = main
    pageable(page, totalPage, "loadUsers");
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
        document.getElementById("avatar").value = linkImage;
        document.getElementById("btnchoosefile").style.backgroundImage = `url('${linkImage}')`;
    }
    document.getElementById("btn-submit").disabled = false
    document.getElementById("btn-submit").innerText = "Lưu User"
}

async function saveUser() {
    var uls = new URL(document.URL)
    var id = uls.searchParams.get("id");
    var user = {
        id: id,
        fullname: document.getElementById("fullName").value,
        email: document.getElementById("email").value,
        phone: document.getElementById("phone").value,
        password: document.getElementById("password").value,
        code: document.getElementById("code").value,
        actived: document.getElementById("actived").checked,
        avatar: document.getElementById("avatar").value,
        authorities: {
            name:document.getElementById("roles").value
        }
    }
    const res = await fetch('http://localhost:8080/api/user/admin/create-update', {
        method: 'POST',
        headers: new Headers({
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json'
        }),
        body: JSON.stringify(user)
    });
    if (res.status < 300) {
        swal({
            title: "Thông báo",
            text: id==null?"thêm user thành công!":"Cập nhật user thành công!",
            type: "success"
        },
        function() {
            window.location.href = 'user'
        });
    } 
    else if(res.status == 417){
        var result = await res.json();
        swal("Thông báo", result.defaultMessage || "Có lỗi xảy ra", "error");
    }
    else {
        var result = await res.json();
        console.log(result.defaultMessage);
        
        if(result && result.message){
            toastr.error(result.message);
        } 
        else if(result && result.defaultMessage){
            toastr.error(result.defaultMessage);
        }
        else {
            toastr.error("Tạo user thất bại");
        }
    }

}


async function loadAUser() {
    var id = window.location.search.split('=')[1];
    if (id != null) {
        var url = `http://localhost:8080/api/user/admin/${id}`;
        const response = await fetch(url, {
            method: 'GET',
            headers: new Headers({
                'Authorization': 'Bearer ' + token
            })
        });
        var result = await response.json();
        console.log(result)
        
        document.getElementById("fullName").value = result.fullname
        document.getElementById("email").value = result.email
        document.getElementById("phone").value = result.phone
        document.getElementById("code").value = result.code
        document.getElementById("actived").checked = result.actived
        document.getElementById("avatar").value = result.avatar
        if(result.avatar){
            document.getElementById("btnchoosefile").style.backgroundImage = `url('${result.avatar}')`;
        }
        document.getElementById("roles").value = result.authorities.name
        document.getElementById("password").value = " ";
        document.getElementById("password").value = "";
    }
}

async function toggleUserStatus(userId, type) {
    const res = await fetch(`http://localhost:8080/api/user/admin/lockOrUnlockUser?id=${userId}`, {
        method: 'POST',
        headers: new Headers({
            'Authorization': 'Bearer ' + token,
        }),
    }); 
    if (res.status < 300) {
        if(type == 1){
            toastr.success("Khóa user thành công");
            document.getElementById(`toggle-${userId}`).setAttribute("onclick", `toggleUserStatus(${userId}, 0)`);
            document.getElementById(`toggle-${userId}`).classList.remove("btn-danger");
            document.getElementById(`toggle-${userId}`).classList.add("btn-success");
            document.getElementById(`toggle-${userId}`).innerHTML = '<i class="bi bi-unlock"></i>';
            document.getElementById(`status-${userId}`).innerHTML = '<span class="badge bg-danger">Đã khóa</span>';
        } else {
            toastr.success("Mở khóa user thành công");
            document.getElementById(`toggle-${userId}`).setAttribute("onclick", `toggleUserStatus(${userId}, 1)`);
            document.getElementById(`toggle-${userId}`).classList.remove("btn-success");
            document.getElementById(`toggle-${userId}`).classList.add("btn-danger");
            document.getElementById(`toggle-${userId}`).innerHTML = '<i class="bi bi-lock"></i>';
            document.getElementById(`status-${userId}`).innerHTML = '<span class="badge bg-success">Đang hoạt động</span>';
        }
    } else {
        toastr.error("Cập nhật trạng thái thất bại");
    }
}

async function deleteUser(id) {
    // 1. Hiển thị thông báo xác nhận trước khi làm gì khác
    swal({
        title: "Bạn có chắc chắn muốn xóa?",
        text: "Hành động này không thể hoàn tác!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Xóa",
        cancelButtonText: "Hủy",
        closeOnConfirm: false // Chờ phản hồi từ server mới đóng alert
    },
    async function(isConfirm) {
        // Nếu người dùng chọn "Xóa"
        if (isConfirm) {
            var url = 'http://localhost:8080/api/user/admin/delete?id=' + id;
            
            try {
                const response = await fetch(url, {
                    method: 'DELETE',
                    headers: new Headers({
                        'Authorization': 'Bearer ' + token
                    })
                });

                if (response.status < 300) {
                    swal("Đã xóa!", "Người dùng đã được xóa khỏi hệ thống.", "success");
                    document.getElementById(`user-row-${id}`).remove();
                } else if (response.status == exceptionCode) {
                    var result = await response.json();
                    swal("Lỗi!", result.defaultMessage, "error");
                } else {
                    swal("Thất bại!", "Có lỗi xảy ra trong quá trình xóa.", "error");
                }
            } catch (error) {
                swal("Lỗi!", "Không thể kết nối đến máy chủ.", "error");
            }
        }
    });
}


async function readFileExcel() {
    const filePath = document.getElementById('formFile')
    const formData = new FormData()
    formData.append("file", filePath.files[0])
    var urlUpload = 'http://localhost:8080/api/user/admin/import-excel';
    const res = await fetch(urlUpload, {
        method: 'POST',
        headers: new Headers({
            'Authorization': 'Bearer ' + token,
        }),
        body: formData
    });
    if (res.status < 300) {
        const result = await res.json();
        
        showImportResult(result);
    } else if (res.status == 417) {
        const result = await res.json();
        showImportResult(result);
    } else {
        toastr.error("Import thất bại");
    }
}


function showImportResult(result) {

    const container = document.getElementById("importResult");
    const messageDiv = document.getElementById("importMessage");
    const successCount = document.getElementById("importSuccessCount");
    const failCount = document.getElementById("importFailCount");
    const totalCount = document.getElementById("importTotalCount");
    const duplicateSection = document.getElementById("duplicateSection");
    const duplicateList = document.getElementById("duplicateList");

    // show container
    container.style.display = "block";

    // message
    messageDiv.innerText = result.message || "";

    if (result.skipped === 0) {
        messageDiv.className = "alert alert-success";
    } else if (result.inserted === 0) {
        messageDiv.className = "alert alert-danger";
    } else {
        messageDiv.className = "alert alert-warning";
    }

    // counts
    totalCount.innerText = Number(result.success) + Number(result.numEmailExist) ?? 0;
    successCount.innerText = result.success ?? 0;
    failCount.innerText = result.numEmailExist ?? 0;

    // skipped emails
    duplicateList.innerHTML = "";

    if (result.listEmailExist && result.listEmailExist.length > 0) {

        duplicateSection.style.display = "block";

        result.listEmailExist.forEach(email => {

            const li = document.createElement("li");
            li.className = "list-group-item list-group-item-danger";
            li.innerHTML = `<i class="bi bi-x-circle me-2"></i>${email}`;

            duplicateList.appendChild(li);

        });

    } else {

        duplicateSection.style.display = "none";

    }

}

async function changePassword() {
    var token = localStorage.getItem("token");
    var oldpass = document.getElementById("oldpass").value
    var newpass = document.getElementById("newpass").value
    var renewpass = document.getElementById("renewpass").value
    var url = 'http://localhost:8080/api/user/all/change-password';
    if (newpass != renewpass) {
        swal("Lỗi", "Mật khẩu mới không trùng khớp", "error");
        return;
    }
    var passw = {
        "oldPass": oldpass,
        "newPass": newpass
    }
    const response = await fetch(url, {
        method: 'POST',
        headers: new Headers({
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json'
        }),
        body: JSON.stringify(passw)
    });
    if (response.status < 300) {
        swal({
                title: "Thông báo",
                text: "cập nhật mật khẩu thành công, hãy đăng nhập lại",
                type: "success"
            },
            function() {
                window.location.reload();
            });
    }
    if (response.status == exceptionCode) {
        var result = await response.json()
        swal("Lỗi", result.defaultMessage, "error");
    }
}