async function loadScore(){
    var regisStudentId = document.getElementById("regisStudentId").value;
    var response = await getMethod(`/api/score_componentApi/teacher/find-by-studentRegis?studentRegisId=${regisStudentId}`)
    var result = await response.json();
    renderScore(result.scored)
    renderNotScore(result.notScored)
}

function renderScore(listScore){
    var main = '';
    for(var i=0; i< listScore.length; i++){
        var item = listScore[i];
        main +=
            `<div class="dashbroad-student-item dashbroad-student-show">
               <div class="d-flex justify-content-between align-items-center mb-1">
                  <div>
                    <div class="dashbroad-student-name">${item.name}</div>
                    <div class="dashbroad-student-percent">${item.percent}% trọng số</div>
                  </div>
                  <div>
                    <input type="number" min="0" max="10" step="0.1" class="dashbroad-student-input" value="${item.point}" id="point-score-${item.id}"/>
                    <button onclick="updateScore(${item.id})" class="btn btn-sm btn-outline-primary"><i class="fa fa-check"></i></button>
                    <button onclick="deleteScore(${item.id})" class="btn btn-sm btn-outline-danger"> X </button>
                  </div>
                </div>
            </div>`
    }
    document.getElementById("scoredList").innerHTML = main;
}

function renderNotScore(listNotScore){
    var main = '';
    for(var i=0; i< listNotScore.length; i++){
        var item = listNotScore[i];
        main +=
            `<div class="dashbroad-student-item dashbroad-student-show">
               <div class="d-flex justify-content-between align-items-center mb-1">
                  <div>
                    <div class="dashbroad-student-name">${item.name}</div>
                    <div class="dashbroad-student-percent">${item.percent}% trọng số</div>
                  </div>
                  <div>
                    <input type="number" min="0" max="10" step="0.1" class="dashbroad-student-input" id="point-not-score-${item.id}"/>
                    <button onclick="addScore(${item.id})" class="btn btn-sm btn-outline-primary"><i class="fa fa-check"></i></button>
                  </div>
                </div>
            </div>`
    }
    document.getElementById("notScoredList").innerHTML = main;
}

async function addScore(idScoreRatio){
    var payload = {
        "point":document.getElementById(`point-not-score-${idScoreRatio}`).value,
        "scoreRatioId":idScoreRatio,
        "studentRegis":{
            "id":document.getElementById("regisStudentId").value
        }
    }
    if (payload.point !== "" && payload.point != null) {
        const point = Number(payload.point);
        if (isNaN(point) || point < 0 || point > 10) {
            swal('Lỗi','⚠️ Điểm phải là số từ 0 đến 10', 'error')
            return;
        }
    }
    var response = await postMethodPayload(`/api/score_componentApi/teacher/create-update`, payload)
    if(response.status < 300){
        swal('Thông báo','Cập nhật điểm cho sinh viên thành công','success');
        loadScore();
    }
}

async function updateScore(idScoreComponent){
    var payload = {
        "id":idScoreComponent,
        "point":document.getElementById(`point-score-${idScoreComponent}`).value,
    }
    if (payload.point !== "" && payload.point != null) {
        const point = Number(payload.point);
        if (isNaN(point) || point < 0 || point > 10) {
            swal('Lỗi','⚠️ Điểm phải là số từ 0 đến 10', 'error')
            return;
        }
    }
    var response = await postMethodPayload(`/api/score_componentApi/teacher/create-update`, payload)
    if(response.status < 300){
        swal('Thông báo','Thay đổi điểm cho sinh viên thành công','success');
        loadScore();
    }
}

async function deleteScore(idScoreComponent){
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
            var response = await deleteMethod(`/api/score_componentApi/teacher/delete?id=${idScoreComponent}`)
            if (response) {
                swal("Đã xóa!", "Điểm đã được xóa khỏi hệ thống.", "success");
                loadScore();
            }
        }
    });
}