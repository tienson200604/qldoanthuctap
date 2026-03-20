async function login() {
    var url = 'http://localhost:8080/api/user/login/email'
    var user = {
        "username": document.getElementById("username").value,
        "password": document.getElementById("password").value,
    }
    const response = await fetch(url, {
        method: 'POST',
        headers: new Headers({
            'Content-Type': 'application/json'
        }),
        body: JSON.stringify(user)
    });
    var result = await response.json();
    
    if (response.status < 300) {
        localStorage.setItem("user", JSON.stringify(result.user));
        localStorage.setItem("token", result.token);
        if (result.user.authorities.name == "ROLE_ADMIN") {
            window.location.href = "/admin/index"
        }
        else if(result.user.authorities.name == "ROLE_TEACHER"){
            window.location.href = "/teacher/index"
        }
        else if(result.user.authorities.name == "ROLE_STUDENT"){
            window.location.href = "/student/index"
        }
    }
    else{
        toastr.warning(result.defaultMessage);
        document.getElementById('message').innerText = result.defaultMessage;
    }
}

async function forgorPassword() {
    var email = document.getElementById("email").value
    var url = 'http://localhost:8080/api/user/public/init-forgotpasss?email=' + email
    const res = await fetch(url, {
        method: 'POST'
    });
    if (res.status < 300) {
        swal({
                title: "",
                text: "Kiểm tra email của bạn",
                type: "success"
            },
            function() {
                window.location.replace("login")
            });
    }
    if (res.status == 417) {
        var result = await res.json()
        toastr.warning(result.defaultMessage);
    }
}

async function datLaiMatKhau() {
    var password = document.getElementById("newPassword").value
    var repassword = document.getElementById("confirmPassword").value
    if(password != repassword){
        Swal("Lỗi", "Mật khẩu không trùng khớp", "error");
        return;
    }
    var uls = new URL(document.URL)
    var email = uls.searchParams.get("email");
    var key = uls.searchParams.get("key");
    var url = 'http://localhost:8080/api/user/public/finish-reset-pass?email=' + email+'&key='+key+'&password='+password
    const res = await fetch(url, {
        method: 'POST'
    });
    if (res.status < 300) {
        swal({
                title: "",
                text: "Đặt lại mật khẩu thành công",
                type: "success"
            },
            function() {
                window.location.replace("login")
            });
    }
    if (res.status == 417) {
        var result = await res.json()
        toastr.warning(result.defaultMessage);
    }
}