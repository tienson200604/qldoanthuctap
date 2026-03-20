async function submitEvaluation() {
    const form = document.getElementById("evaluationForm");
    const data = {
        id: document.getElementById("idrate").value,
        q1: parseInt(form.querySelector('input[name="q1"]:checked')?.value),
        q2: parseInt(form.querySelector('input[name="q2"]:checked')?.value),
        q3: parseInt(form.querySelector('input[name="q3"]:checked')?.value),
        comment: form.querySelector('textarea[name="comment"]').value.trim(),
        studentRegis:{
            id:document.getElementById("studentRegisIdRate").value
        }
    }
    var response = await postMethodPayload(`/api/rate/student/create-update`, data)
    if(response.status < 300){
        swal({
            title: "Thông báo",
            text: "gửi đánh giá thành công",
            type: "success"
        },
        function() {
            window.location.reload()
        });
    }
}