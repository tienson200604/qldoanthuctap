async function addRelate(){
    var payload = {
        "id":document.getElementById("idRelate").value,
        "deadline":document.getElementById("relate-document-deadline").value,
        "name":document.getElementById("relate-document-name").value,
        "description":tinyMCE.get('contentRelateDocument').getContent(),
        "semesterTeacher":{
            "id":document.getElementById("semesterTeacherIdRelate").value
        }
    }
    var response = await postMethodPayload('/api/related_document/teacher/create-update', payload);
    if(response.status < 300){
        swal({
                title: "Thông báo",
                text: payload.id == null?"Thêm yêu cầu thành công":"Cập nhật yêu cầu thành công",
                type: "success"
            },
            function() {
                window.location.reload();
            });
    }
}

async function loadRelate() {
    var semesterTeacherId = document.getElementById("semesterTeacherIdRelate").value
    var url = `/api/related_document/student-teacher/find-all?semesterTeacherId=${semesterTeacherId}`
    var response = await getMethod(url)
    if(response == null){
        document.getElementById("list-data-work").innerHTML = '<tr><td colspan="8" class="text-center">Lỗi khi tải dữ liệu</td></tr>';
    }
    var list = await response.json();
    if(list.length == 0){
        document.getElementById("list-data-work").innerHTML = '<tr><td colspan="8" class="text-center">Không có dữ liệu</td></tr>';
        return;
    }
    var main = '';
    var totalStudent = document.getElementById("totalStudentRelate").value
    for (i = 0; i < list.length; i++) {
        var b = list[i];
        main += `<div class="teacher-work-group" id="data-work-row-${b.id}">
                <h4 class="teacher-work-date mb-3 mt-5">${formatTime(b.deadline)[1]} - <strong>${formatTime(b.deadline)[2]}</strong></h4>

                <div class="teacher-work-card">
                    <div class="teacher-work-content">
                        <h6 class="teacher-work-name">Loại giấy tờ: ${b.name}</h6>
                        <p class="mb-1">Mã yêu cầu: <b>${b.id}</b></p>
                        <p class="mb-1">Hạn cuối lúc: <b>${formatTime(b.deadline)[0]}</b></p>
                        <small>Nộp đúng hạn: <strong class="text-green">${b.onTimeCount}</strong> - Nộp muộn: <strong class="text-red">${b.outTimeCount}</strong>
                        - Chưa nộp <strong class="text-yellow">${Number(totalStudent) - Number(b.onTimeCount + b.outTimeCount)}</strong></small>
                    </div>

                    <div class="teacher-work-actions">
                    ${calDeadline(b.deadline) == true?`<span class="badge bg-success expired">Còn hạn</span>`:`<span class="teacher-work-badge expired">Hết hạn</span>`}
                        <div class="teacher-work-btn-group">
                            <button onclick="loadRelateDetail(${b.id})" data-bs-toggle="modal" data-bs-target="#modal-add-relate-document" class="btn btn-sm btn-warning"><i class="bi bi-pencil"></i></button>
                            <button onclick="deleteRelate(${b.id})" class="btn btn-sm btn-danger"><i class="bi bi-x"></i></button>
                            <a href="/teacher/document-student/${b.id}" class="btn btn-sm btn-warning"><i class="fa fa-eye"></i></a>
                        </div>
                    </div>
                </div>
            </div>`
    }
    document.getElementById("list-data-relate-document").innerHTML = main
}


async function loadRelateDetail(id){
    var response = await getMethod(`/api/related_document/student-teacher/find-by-id?id=${id}`)
    var result = await response.json();
    document.getElementById("idRelate").value = result.id;
    document.getElementById("relate-document-name").value = result.name;
    document.getElementById("relate-document-deadline").value = result.deadline;
    tinyMCE.get('contentRelateDocument').setContent(result.description)
}

function clearRelate(){
    if(document.getElementById("idRelate").value != ""){
        document.getElementById("idRelate").value = "";
        document.getElementById("relate-document-deadline").value = "";
        tinyMCE.get('contentRelateDocument').setContent("")
    }
}

async function deleteRelate(id) {
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
                var response = await deleteMethod(`/api/related_document/teacher/delete?id=${id}`)
                if (response) {
                    swal({
                            title: "Đã xóa!",
                            text: "Yêu cầu nộp đã được xóa khỏi hệ thống!",
                            type: "success"
                        },
                        function() {
                            window.location.reload();
                        });
                }
            }
        });
}