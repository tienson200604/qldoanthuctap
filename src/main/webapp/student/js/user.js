async function updateInfor(){
    var fullname = document.getElementById("fullname").value;
    var phone = document.getElementById("phone").value;
    var className = document.getElementById("className").value;
    var avatar = document.getElementById("avatar").value;

    if(fullname.trim() === ""){
        swal('Lỗi','Vui lòng nhập họ và tên','error'); return;
    }
    if(phone.trim() === ""){
        swal('Lỗi','Vui lòng nhập số điện thoại','error'); return;
    }

    var btn = document.getElementById("btn-update-infor");
    var originalHtml = btn.innerHTML;
    btn.disabled = true;
    btn.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Đang lưu...';

    var payload = {
        "fullname":fullname,
        "phone":phone,
        "avatar":avatar,
        "classname":className,
    }
    try {
        var response = await postMethodPayload('/api/user/all/update-infor', payload)
        if (response && response.status < 300) {
            swal('Thông báo', 'Cập nhật thông tin thành công', 'success');
        }
    } catch (error) {
        console.error(error);
        // postMethodPayload usually handles its own errors with swal, 
        // but if it throws (e.g. network error), we should handle it here.
    } finally {
        btn.disabled = false;
        btn.innerHTML = originalHtml;
    }
}


async function chooseAvatar(){
    document.getElementById("btn-submit").innerText = "Đang tải ảnh..."
    const filePath = document.getElementById('avatarInput')
    const formData = new FormData()
    formData.append("file", filePath.files[0])
    var urlUpload = firstUrl + '/api/public/upload-file';
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
    var currentPassword = document.getElementById("currentPassword").value.trim();
    var newPassword = document.getElementById("newPassword").value.trim();
    var confirmPassword = document.getElementById("confirmPassword").value.trim();
    var passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*(),.?":{}|<>])\S{8,}$/;

    if(currentPassword === ''){
        swal('Lỗi','Vui lòng nhập mật khẩu hiện tại','error'); return;
    }
    if(newPassword === ''){
        swal('Lỗi','Vui lòng nhập mật khẩu mới','error'); return;
    }
    if(confirmPassword === ''){
        swal('Lỗi','Vui lòng nhập xác nhận mật khẩu','error'); return;
    }
    if(newPassword !== confirmPassword){
        swal('Lỗi','Xác nhận mật khẩu không khớp','error'); return;
    }
    if(!passwordRegex.test(newPassword)){
        swal('Lỗi','Mật khẩu mới phải có ít nhất 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt','error'); return;
    }
    var payload = {
        "oldPass":currentPassword,
        "newPass":newPassword,
        "confirmPass":confirmPassword,
    }
    var response = await postMethodPayload('/api/user/all/change-password', payload)
    if(response && response.status < 300){
        swal('Thông báo','Đổi mật khẩu thành công','success');
    }
}
