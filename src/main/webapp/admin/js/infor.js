var token = localStorage.getItem("token");

async function loadMyInfor() {
    var url = 'http://localhost:8080/api/user/all/user-logged';
    const response = await fetch(url, {
        method: 'POST',
        headers: new Headers({
            'Authorization': 'Bearer ' + token
        })
    });
    var userCount = await response.json();
    document.getElementById("fullname").value = userCount.fullname;
    document.getElementById("phone").value = userCount.phone;
    document.getElementById("email").value = userCount.email;
    document.getElementById("username").value = userCount.username;
    document.getElementById("code").value = userCount.code;
    document.getElementById("avatarUrl").value = userCount.avatar;
    if(userCount.avatar != null && userCount.avatar != "") {
        document.getElementById("avatarPreview").src = userCount.avatar;
    }
    
    // Cập nhật text hiển thị
    document.getElementById("displayFullname").innerText = userCount.fullname;
    document.getElementById("displayUsername").innerText = "@" + userCount.username;
    document.getElementById("displayEmail").innerText = userCount.email;
}

async function uploadAvatar() {
    const filePath = document.getElementById('avatarInput')
    const formData = new FormData()
    formData.append("file", filePath.files[0])
    var urlUpload = 'http://localhost:8080/api/public/upload-file';
    
    const res = await fetch(urlUpload, {
        method: 'POST',
        body: formData
    });
    
    if (res.status < 300) {
        const linkImage = await res.text();
        document.getElementById("avatarUrl").value = linkImage;
        document.getElementById("avatarPreview").src = linkImage;
        toastr.success("Tải ảnh thành công");
    } else {
        toastr.error("Tải ảnh thất bại");
    }
}

async function updateProfile() {
    const btn = document.getElementById("btnUpdate");
    btn.disabled = true;
    btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span> Đang lưu...';

    const userUpdate = {
        fullname: document.getElementById("fullname").value,
        phone: document.getElementById("phone").value,
        avatar: document.getElementById("avatarUrl").value
    };

    const res = await fetch('http://localhost:8080/api/user/all/update-infor', {
        method: 'POST',
        headers: new Headers({
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json'
        }),
        body: JSON.stringify(userUpdate)
    });

    if (res.status < 300) {
        const result = await res.json();
        // Cập nhật localStorage
        localStorage.setItem("user", JSON.stringify(result));
        
        swal({
            title: "Thành công",
            text: "Cập nhật thông tin cá nhân thành công!",
            type: "success"
        }, function() {
            window.location.reload();
        });
    } else {
        const result = await res.json();
        toastr.error(result.defaultMessage || "Cập nhật thất bại");
    }
    
    btn.disabled = false;
    btn.innerHTML = '<i class="bi bi-save me-2"></i> Lưu thay đổi';
}
