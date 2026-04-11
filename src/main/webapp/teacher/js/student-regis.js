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
        title: "Lý do từ chối",
        text: "Sinh viên sẽ nhận được thông báo kèm lý do này",
        type: "input",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Từ chối",
        cancelButtonText: "Hủy",
        closeOnConfirm: false,
        inputPlaceholder: "Nhập lý do từ chối"
    }, async function(inputValue) {
        if (inputValue === false) {
            return false;
        }
        if (!inputValue || !inputValue.trim()) {
            swal.showInputError("Vui lòng nhập lý do từ chối");
            return false;
        }
        var response = await postMethodTextPlan(`/api/student-regis/teacher/reject?id=${idStudentRegis}`, inputValue.trim())
        if(response != null && response.status < 300){
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

async function completeStudent(e){
    var idStudentRegis = e.getAttribute("data-student-regis")
    swal({
        title: "Xác nhận",
        text: "Bạn có chắc chắn muốn đánh dấu sinh viên này đã hoàn thành thực tập?",
        type: "success",
        showCancelButton: true,
        confirmButtonColor: "#28a745",
        confirmButtonText: "Hoàn thành",
        cancelButtonText: "Hủy",
        closeOnConfirm: false
    }, async function() {
        var response = await postMethod(`/api/student-regis/teacher/complete?id=${idStudentRegis}`)
        if(response.status < 300){
            swal({
                    title: "Thông báo",
                    text: 'Đã cập nhật trạng thái hoàn thành',
                    type: "success"
                },
                function() {
                    window.location.reload()
                });
        }
    });
}
