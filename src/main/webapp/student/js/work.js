var listWork = [];
var feedbackModalOpened = false;
async function loadWork(page) {
    var size = 10;
    var sort = document.getElementById("work-sort").value
    var search = document.getElementById("work-search").value
    var semesterTeacherId = document.getElementById("semesterTeacherId").value
    var url = `/api/work-process/student/find-all?page=${page}&size=${size}&semesterTeacherId=${semesterTeacherId}&sort=${sort}`
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
    listWork = list;
    if(list.length == 0){
        document.getElementById("list-data-work").innerHTML = '<tr><td colspan="8" class="text-center">Không có dữ liệu</td></tr>';
        return;
    }
    var main = '';
    for (i = 0; i < list.length; i++) {
        var b = list[i];
        main += `<div class="teacher-work-group pointer">
            <h4 class="teacher-work-date mb-3 mt-5">
                ${formatTime(b.deadline)[1]} - <strong>${formatTime(b.deadline)[2]}</strong> 
                - ${b.workProcessStudentResponse?.replayDate == null?'':
            `<button class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#modal-replay-std" onclick="viewReplay(${b.workProcessStudentResponse?.id})">Xem phản hồi</button>`}
            </h4>
        
            <div class="teacher-work-card ${b.isSubmitted ? 'submitted' : 'not-submitted'}" data-bs-toggle="modal" data-bs-target="#modal-send-word"
            onclick="clickWork(${b.id})">
                <div class="teacher-work-content">
                    <h6 class="teacher-work-name">${b.title}</h6>
                    <p class="mb-1">Mã tiến độ: <b>${b.id}</b></p>
                    <p class="mb-1">Hạn cuối lúc: <b>${formatTime(b.deadline)[0]}</b></p>
                    ${b.isSubmitted == false?'':`Nộp vào lúc <strong>${b.workProcessStudentResponse?.createdDate}</strong>`}
                </div>
        
                <div class="teacher-work-actions">
        
                    ${b.isSubmitted
                    ? `<span class="badge bg-success">Đã nộp</span>`
                    : `<span class="badge bg-danger">Chưa nộp</span>`
                }
        
                    ${calDeadline(b.deadline)
                    ? `<span class="badge bg-success ms-1">Còn hạn</span>`
                    : `<span class="badge bg-secondary ms-1">Hết hạn</span>`
                }
        
                </div>
            </div>
        </div>`;
    }
    document.getElementById("list-data-work").innerHTML = main
    var mainpage = ''
    for (i = 1; i <= totalPage; i++) {
        mainpage += `<li onclick="loadWork(${(Number(i) - 1)})" class="page-item ${Number(i)-1 == page?'active':''}"><a class="page-link" href="#">${i}</a></li>`
    }
    document.getElementById("pagination-work").innerHTML = mainpage
    openFeedbackFromUrl();
}

function viewReplay(workProcessStudentId){
    for(i=0 ;i<listWork.length; i++){
        if(listWork[i].workProcessStudentResponse != null){
            if(listWork[i].workProcessStudentResponse.id == workProcessStudentId){
                renderReplayModal(listWork[i].workProcessStudentResponse);
            }
        }
    }
}

function renderReplayModal(workProcessStudentResponse){
    document.getElementById("ngayphanhoi").innerHTML = formatReplayDate(workProcessStudentResponse.replayDate);
    document.getElementById("noidungphanhoi").innerHTML = workProcessStudentResponse.replay || "Chưa có nội dung phản hồi";
}

function formatReplayDate(replayDate){
    if(replayDate == null){
        return "";
    }
    if(Array.isArray(replayDate)){
        const date = new Date(
            replayDate[0],
            replayDate[1] - 1,
            replayDate[2],
            replayDate[3] || 0,
            replayDate[4] || 0,
            replayDate[5] || 0
        );
        return date.toLocaleString("vi-VN");
    }
    const date = new Date(replayDate);
    if(!isNaN(date.getTime())){
        return date.toLocaleString("vi-VN");
    }
    return replayDate;
}

function clearFeedbackQueryParam(){
    const currentUrl = new URL(window.location.href);
    currentUrl.searchParams.delete("feedbackId");
    history.replaceState(null, "", currentUrl.pathname + currentUrl.search + currentUrl.hash);
}

async function openFeedbackFromUrl(){
    if(feedbackModalOpened){
        return;
    }
    const currentUrl = new URL(window.location.href);
    const feedbackId = currentUrl.searchParams.get("feedbackId");
    if(!feedbackId){
        return;
    }

    for (let i = 0; i < listWork.length; i++) {
        const response = listWork[i].workProcessStudentResponse;
        if(response != null && String(response.id) === String(feedbackId)){
            feedbackModalOpened = true;
            renderReplayModal(response);
            new bootstrap.Modal(document.getElementById("modal-replay-std")).show();
            clearFeedbackQueryParam();
            return;
        }
    }

    const response = await getMethod(`/api/work-process-student/student/find-my-feedback?id=${feedbackId}`);
    if(response == null){
        return;
    }
    const result = await response.json();
    if(result != null){
        feedbackModalOpened = true;
        renderReplayModal(result);
        new bootstrap.Modal(document.getElementById("modal-replay-std")).show();
        clearFeedbackQueryParam();
    }
}

async function clickWork(idWorkProcess){
    document.getElementById("idWorkProcess").value = idWorkProcess
    var response = await getMethod(`/api/work-process-student/student-teacher/find-by-workprocess-id?workProcessId=${idWorkProcess}`)
    if(response.status < 300){
        var result = await response.json();
        if(result.data != null){
            document.getElementById("workstdtitle").value = result.data.title
            document.getElementById("percentstdwork").value = result.data.percent
            tinyMCE.get('contentStdTienDo').setContent(result.data.content)
            updateRange(document.getElementById("percentstdwork"));
        }
        else{
            clearDataStdWork();
            updateRange(document.getElementById("percentstdwork"));
        }
    }
}

function clearDataStdWork(){
    document.getElementById("workstdtitle").value = ""
    document.getElementById("percentstdwork").value = 50
    tinyMCE.get('contentStdTienDo').setContent("")
}

async function addStudentWork(){
    var payload = {
        "title":document.getElementById("workstdtitle").value,
        "content":tinyMCE.get('contentStdTienDo').getContent(),
        "percent":document.getElementById("percentstdwork").value,
        "workProcessId":document.getElementById("idWorkProcess").value,
    }
    var response = await postMethodPayload('/api/work-process-student/student/create-update', payload);
    if(response.status < 300){
        swal({
                title: "Thông báo",
                text: payload.id == null?"Thêm thành công":"Cập nhật thành công",
                type: "success"
            },
            function() {
                window.location.reload();
            });
    }
}
