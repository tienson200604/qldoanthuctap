async function acceptStudent(e){
    var idStudentRegis = e.getAttribute("data-student-regis")
    swal({
        title: "Xác nhận",
        text: "Bạn có chắc chắn muốn duyệt yêu cầu đăng ký này?",
        type: "info",
        showCancelButton: true,
        confirmButtonColor: "#3498db",
        confirmButtonText: "Duyệt",
        cancelButtonText: "Hủy",
        closeOnConfirm: false
    }, async function() {
        var response = await postMethod(`/api/student-regis/teacher/accept?id=${idStudentRegis}`)
        if(response.status < 300){
            swal({
                title: "Thông báo",
                text: 'Duyệt đăng ký thành công',
                type: "success"
            },
            function() {
                window.location.reload()
            });
        }
    });
}

async function rejectStudent(e){
    var idStudentRegis = e.getAttribute("data-student-regis")
    swal({
        title: "Xác nhận",
        text: "Bạn có chắc chắn muốn từ chối yêu cầu đăng ký này?",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Từ chối",
        cancelButtonText: "Hủy",
        closeOnConfirm: false
    }, async function() {
        var response = await postMethod(`/api/student-regis/teacher/reject?id=${idStudentRegis}`)
        if(response.status < 300){
            swal({
                title: "Thông báo",
                text: 'Đã từ chối yêu cầu đăng ký',
                type: "success"
            },
            function() {
                window.location.reload()
            });
        }
    });
}