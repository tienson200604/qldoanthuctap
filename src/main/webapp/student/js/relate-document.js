var relateList = [];
async function loadRelate() {
    var semesterTeacherId = document.getElementById("semesterTeacherIdRelate").value
    var url = `/api/related_document/student/find-all?semesterTeacherId=${semesterTeacherId}`
    var response = await getMethod(url)
    if(response == null){
        document.getElementById("list-data-work").innerHTML = '<tr><td colspan="8" class="text-center">Lỗi khi tải dữ liệu</td></tr>';
    }
    var list = await response.json();
    relateList = list
    if(list.length == 0){
        document.getElementById("list-data-work").innerHTML = '<tr><td colspan="8" class="text-center">Không có dữ liệu</td></tr>';
        return;
    }
    var main = '';
    for (i = 0; i < list.length; i++) {
        var b = list[i];
        main += `<div class="teacher-work-group pointer">
                <h4 class="teacher-work-date mb-3 mt-5">${formatTime(b.deadline)[1]} - <strong>${formatTime(b.deadline)[2]}</strong></h4>

                <div class="teacher-work-card">
                    <div class="teacher-work-content">
                        <h6 class="teacher-work-name">Loại giấy tờ: ${b.name}</h6>
                        <p class="mb-1">Mã yêu cầu: <b>${b.id}</b></p>
                        <p class="mb-1">Hạn cuối lúc: <b>${formatTime(b.deadline)[0]}</b></p>
                        <p class="mb-1">Trạng thái: ${b.isSubmitted == true?`<b style="color: green">Đã nộp</b> - Lúc: ${b.relatedDocumentStudentResponse.createdDate}`:`<b style="color: red">Chưa nộp</b>`}
                        </p>
                    </div>

                    <div class="teacher-work-actions">
                    ${calDeadline(b.deadline) == true?`<span class="badge bg-success expired">Còn hạn</span>`:`<span class="teacher-work-badge expired">Hết hạn</span>`}

                        <div class="teacher-work-btn-group">
                            <button onclick="clickUploadFile(${b.id})" class="btn btn-sm btn-warning" title="Tải file"><i class="fa fa-upload"></i></button>
                            <input data-idFile="${b.id}" onchange="changeFileStd(this)" type="file" id="uploadfilestd-${b.id}" hidden>
                        </div>
                    </div>
                </div>
            </div>`
    }
    document.getElementById("list-data-relate-document").innerHTML = main
}

function clickUploadFile(id){
    document.getElementById(`uploadfilestd-${id}`).click()
}

async function changeFileStd(e){
    var relatedDocumentsId = e.getAttribute("data-idFile");
    const file = e.files[0]; // Captura o objeto File

    if (file) {
        const fileName = file.name;
        const fileType = file.type;


        const formData = new FormData();
        formData.append("file", file);
        var urlUpload = 'http://localhost:8080/api/public/upload-file';
        const res = await fetch(urlUpload, {
            method: 'POST',
            body: formData
        });
        if(res.status < 300){
            const linkImage = await res.text();
            var payload = {
                "fileUrl":linkImage,
                "fileName":fileName,
                "fileType":fileType,
                "relatedDocuments":{
                    "id":relatedDocumentsId
                },
            }
            var response = await postMethodPayload(`/api/related_document_student/student/create`, payload);
            if(response.status < 300){
                swal({
                    title: "Thông báo",
                    text: "Upload file thành công",
                    type: "success"
                },
                function() {
                    window.location.reload();
                });
            }
        }
    }
}