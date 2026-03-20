async function addWork(){
    var payload = {
        "id":document.getElementById("idwork").value,
        "deadline":document.getElementById("work-deadline").value,
        "title":document.getElementById("work-title").value,
        "description":tinyMCE.get('contentTienDo').getContent(),
        "semesterTeacherId":document.getElementById("semesterTeacherId").value,
    }
    var response = await postMethodPayload('/api/work-process/teacher/create-update', payload);
    if(response.status < 300){
        swal({
            title: "Thông báo",
            text: payload.id == null?"Thêm tiến độ công việc thành công":"Cập nhật tiến độ công việc thành công",
            type: "success"
        },
        function() {
            window.location.reload();
        });
    }
}

async function loadWork(page) {
    var size = 10;
    var sort = document.getElementById("work-sort").value
    var search = document.getElementById("work-search").value
    var semesterTeacherId = document.getElementById("semesterTeacherId").value
    var url = `/api/work-process/student-teacher/find-all?page=${page}&size=${size}&semesterTeacherId=${semesterTeacherId}&sort=${sort}`
    if(search != null && search != ''){
        url += `&search=${search}`;
    }
    var response = await getMethod(url)
    if(response == null){
        document.getElementById("list-data-work").innerHTML = '<tr><td colspan="8" class="text-center">Lỗi khi tải dữ liệu</td></tr>';
    }
    var result = await response.json();
    var totalPage = result.totalPages
    var list = result.content;
    if(list.length == 0){
        document.getElementById("list-data-work").innerHTML = '<tr><td colspan="8" class="text-center">Không có dữ liệu</td></tr>';
        return;
    }
    var main = '';
    var totalStudent = document.getElementById("totalStudent").value
    for (i = 0; i < list.length; i++) {
        var b = list[i];
        main += `<div class="teacher-work-group" id="data-work-row-${b.id}">
                <h4 class="teacher-work-date mb-3 mt-5">${formatTime(b.deadline)[1]} - <strong>${formatTime(b.deadline)[2]}</strong></h4>

                <div class="teacher-work-card">
                    <div class="teacher-work-content">
                        <h6 class="teacher-work-name">${b.title}</h6>
                        <p class="mb-1">Mã tiến độ: <b>${b.id}</b></p>
                        <p class="mb-1">Hạn cuối lúc: <b>${formatTime(b.deadline)[0]}</b></p>
                        <small>Nộp đúng hạn: <strong class="text-green">${b.onTimeCount}</strong> - Nộp muộn: <strong class="text-red">${b.outTimeCount}</strong>
                        - Chưa nộp <strong class="text-yellow">${Number(totalStudent) - Number(b.onTimeCount + b.outTimeCount)}</strong></small>
                    </div>

                    <div class="teacher-work-actions">
                    ${calDeadline(b.deadline) == true?`<span class="badge bg-success expired">Còn hạn</span>`:`<span class="teacher-work-badge expired">Hết hạn</span>`}

                        <div class="teacher-work-btn-group">
                            <button onclick="loadWorkDetail(${b.id})" data-bs-toggle="modal" data-bs-target="#modal-add-word" class="btn btn-sm btn-warning"><i class="bi bi-pencil"></i></button>
                            <button onclick="deleteWork(${b.id})" class="btn btn-sm btn-danger"><i class="bi bi-x"></i></button>
                            <a href="/teacher/work-student/${b.id}" class="btn btn-sm btn-warning"><i class="fa fa-eye"></i></a>
                        </div>
                    </div>
                </div>
            </div>`
    }
    document.getElementById("list-data-work").innerHTML = main
    var mainpage = ''
    for (i = 1; i <= totalPage; i++) {
        mainpage += `<li onclick="loadWork(${(Number(i) - 1)})" class="page-item ${Number(i)-1 == page?'active':''}"><a class="page-link" href="#">${i}</a></li>`
    }
    document.getElementById("pagination-work").innerHTML = mainpage
}

async function loadWorkDetail(id){
    var response = await getMethod(`/api/work-process/student-teacher/find-by-id?id=${id}`)
    var result = await response.json();
    document.getElementById("idwork").value = result.id;
    document.getElementById("work-title").value = result.title;
    document.getElementById("work-deadline").value = result.deadline;
    tinyMCE.get('contentTienDo').setContent(result.description)
}

function clearDataWork(){
    document.getElementById("idwork").value = "";
    document.getElementById("work-title").value = "";
    document.getElementById("work-deadline").value = "";
    tinyMCE.get('contentTienDo').setContent("")
}

async function deleteWork(id) {
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
                var response = await deleteMethod(`/api/work-process/teacher/delete?id=${id}`)
                if (response) {
                    swal({
                        title: "Đã xóa!",
                        text: "Tiến độ đã được xóa khỏi hệ thống!",
                        type: "success"
                    },
                    function() {
                        window.location.reload();
                    });
                }
            }
        });
}

async function sendFeedback(id, btn) {
    const textarea = btn.parentElement.querySelector(".feedback-input");
    const content = textarea.value;

    if (!content.trim()) {
        swal('Lỗi',"Vui lòng nhập phản hồi", 'error');
        return;
    }
    var response = await postMethodTextPlan(`/api/work-process-student/teacher/replay/${id}`, content)
    if(response.status < 300){
        swal('Thông báo',"Đã gửi feedback cho sinh viên!","success");
    }
}