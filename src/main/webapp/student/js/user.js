async function updateInfor(){
    var payload = {
        "fullname":document.getElementById("fullname").value,
        "phone":document.getElementById("phone").value,
        "avatar":document.getElementById("avatar").value,
    }
    var response = await postMethodPayload('/api/user/all/update-infor',payload)
    if(response.status < 300){
        swal('Thông báo','Cập nhật thông tin thành công','success');
    }
}


async function chooseAvatar(){
    document.getElementById("btn-submit").innerText = "Đang tải ảnh..."
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
        document.getElementById("avatar").value = linkImage;
        document.getElementById("avatarPreview").src = linkImage
        updateInfor();
    }
    document.getElementById("btn-submit").innerText = "Cập nhật ảnh"
}

async function changePassword(){
    if(document.getElementById("newPassword").value != document.getElementById("confirmPassword").value){
        swal('Lỗi','Mật khẩu mới không chính xác','error'); return;
    }
    var payload = {
        "oldPass":document.getElementById("currentPassword").value,
        "newPass":document.getElementById("newPassword").value,
    }
    var response = await postMethodPayload('/api/user/all/change-password', payload)
    if(response.status < 300){
        swal('Thông báo','Đổi mật khẩu thành công','success');
    }
}