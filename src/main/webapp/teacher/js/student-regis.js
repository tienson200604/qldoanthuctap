async function acceptStudent(e){
    var idStudentRegis = e.getAttribute("data-student-regis")
    var response = await postMethod(`/api/student-regis/teacher/accept?id=${idStudentRegis}`)
    if(response.status < 300){
        swal({
            title: "Thông báo",
            text: 'Cập nhật trạng thái thành công',
            type: "success"
        },
        function() {
            window.location.reload()
        });
    }
}