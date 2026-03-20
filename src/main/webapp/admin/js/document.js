var token = localStorage.getItem("token");
var size = 10;
async function loadDocument(page) {
    var url = `/api/document/admin/all?page=${page}&size=${size}`
    var search = document.getElementById("search").value
    var status = document.getElementById("status").value
    var category = document.getElementById("category").value
    if(search != null && search != ''){
        url += `&search=${search}`
    }
    if(status != null && status != ''){
        url += `&status=${status}`
    }
    if(category != null && category != ''){
        url += `&categoryId=${category}`
    }
    var response = await getMethod(url)
    if(response == null){
        document.getElementById("list-data").innerHTML = '<tr><td colspan="8" class="text-center">Lỗi khi tải dữ liệu</td></tr>';
    }
    var result = await response.json();
    var totalPage = result.totalPages
    var list = result.content;
    if(list.length == 0){
        document.getElementById("list-data").innerHTML = '<tr><td colspan="8" class="text-center">Không có dữ liệu</td></tr>';
        return;
    }
    var main = '';
    for (i = 0; i < list.length; i++) {
        main += ` <tr id="data-row-${list[i].id}">
                    <td>${list[i].linkImage == null || list[i].linkImage ==''?'':`<img src="${list[i].linkImage}" style="width:40px">`}</td>
                    <td>${list[i].id}</td>
                    <td>${list[i].name}</td>
                    <td>${list[i].category.name}</td>
                    <td>${list[i].createdDate ? list[i].createdDate : ''}</td>
                    <td><span class="badge" style="background-color:${list[i].status.color}">${list[i].status.displayName}</span></td>
                    <td>${list[i].numberView}</td>
                    <td>${list[i].user.email}</td>
                    <td class="sticky-col">
                        <button onclick="deleteDocument(${list[i].id})" class="btn btn-sm btn-danger"><i class="bi bi-trash"></i></button>
                        <a href="add-document?id=${list[i].id}" class="btn btn-sm btn-primary"><i class="bi bi-pencil"></i></a>
                    </td>
                </tr>`
    }
    document.getElementById("list-data").innerHTML = main
    var mainpage = ''
    for (i = 1; i <= totalPage; i++) {
        mainpage += `<li onclick="loadDocument(${(Number(i) - 1)})" class="page-item ${Number(i)-1 == page?'active':''}"><a class="page-link" href="#">${i}</a></li>`
    }
    document.getElementById("pagination").innerHTML = mainpage
}

async function loadCategorySelect() {
    var response = await getMethod(`/api/category/public/all-by-type?type=DOCUMENT`)
    var list = await response.json();
    var main = '';
    for (i = 0; i < list.length; i++) {
        main += `<option value="${list[i].id}">${list[i].name}</option>`
    }
    document.getElementById("category").innerHTML = main
    $('#category').select2({
        theme: "bootstrap-5",
        width: '100%', 
        templateSelection: (data) => data.text 
    });
}

async function loadDocumentStatus() {
    var response = await getMethod(`/api/document/admin-teacher/document-status`)
    var list = await response.json();
    const container = document.getElementById("statusContainer");
    container.innerHTML = "";
    list.forEach((status, index) => {
        const btn = document.createElement("div");
        btn.className = "status-btn";
        btn.id = `status-${status.name}`;
        btn.innerText = status.displayName;

        // animation delay nhẹ
        btn.style.animation = `popIn 0.3s ease forwards`;
        btn.style.animationDelay = `${index * 0.05}s`;

        btn.addEventListener("click", () => {
            document.querySelectorAll(".status-btn").forEach(b => {
                b.classList.remove("active");
                b.style.background = "#f8f9fa";
                b.style.borderColor = "transparent";
                b.style.color = "#000";
            });

            btn.classList.add("active");
            btn.style.background = status.color;
            btn.style.borderColor = status.color;
            btn.style.color = "#fff";

            var selectedStatus = status.name;
            document.getElementById("status").value = selectedStatus;
            console.log("Đã chọn:", selectedStatus);
        });

        container.appendChild(btn);
    });
}

async function loadInit() {
    var response = await getMethod(`/api/category/public/all-by-type?type=DOCUMENT`)
    var list = await response.json();
    var main = '<option value="">-- Chọn danh mục --</option>';
    for (i = 0; i < list.length; i++) {
        main += `<option value="${list[i].id}">${list[i].name}</option>`
    }
    document.getElementById("category").innerHTML = main
    $('#category').select2({
        theme: "bootstrap-5",
        width: '100%', 
        templateSelection: (data) => data.text 
    });

    var response = await getMethod(`/api/document/admin-teacher/document-status`)
    var list = await response.json();
    var main = '<option value="">-- Chọn trạng thái --</option>';
    for (i = 0; i < list.length; i++) {
        main += `<option value="${list[i].name}">${list[i].displayName}</option>`
    }
    document.getElementById("status").innerHTML = main
    $('#status').select2({
        theme: "bootstrap-5",
        width: '100%', 
        templateSelection: (data) => data.text 
    });
}


async function saveDocument() {
    var uls = new URL(document.URL)
    var id = uls.searchParams.get("id");
    var payload = {
        "id": id,
        "name": document.getElementById("name").value,
        "description": tinyMCE.get('editor').getContent(),
        "categoryId": document.getElementById("category").value,
        "linkImage": document.getElementById("avatar").value,
        "status": document.getElementById("status").value,
        "details":[]
    }
    for(i=0; i< listLinkResponse.length; i++){
        payload.details.push({
            "name":listLinkResponse[i].oriName,
            "fileType":listLinkResponse[i].type,
            "linkFile":listLinkResponse[i].link,
        })
    }
    console.log(payload);
    
    var response = await postMethodPayload(`/api/document/admin-teacher/create-update`, payload)
    if (response != null) {
        swal({
            title: "Thông báo",
            text: id==null?"Thêm tài liệu thành công!":"Cập nhật tài liệu thành công",
            type: "success"
        },
        function() {
            window.location.replace('document');
        });
    }
}

async function loadADocument() {
    var id = window.location.search.split('=')[1];
    if (id != null) {
        var response = await getMethod(`/api/document/admin-teacher/find-by-id?id=${id}`)
        if(response == null){
            return;
        }
        var result = await response.json();
        console.log(result);
        
        document.getElementById("name").value = result.name
        $("#category").val(result.category.id).change()
        document.getElementById("status").value = result.status.name
        document.getElementById(`status-${result.status.name}`).click();
        if(result.linkImage){
            document.getElementById("avatar").value = result.linkImage
            document.getElementById("btnchoosefile").style.backgroundImage = `url('${result.linkImage}')`;
        }
        tinyMCE.get('editor').setContent(result.description)
        renderListFileFromDetails(result.documentDetails)
    }
}

async function deleteDocument(id) {
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
            var response = await deleteMethod(`/api/document/admin-teacher/delete?id=${id}`)
            if (response) {
                swal("Đã xóa!", "Tài liệu đã được xóa khỏi hệ thống.", "success");
                document.getElementById(`data-row-${id}`).remove();
            }
        }
    });
}

async function chooseAvatar(){
    document.getElementById("btn-submit").disabled = true
    document.getElementById("btn-submit").innerText = "Đang tải ảnh..."
    const filePath = document.getElementById('fileanhdaidientl')
    const formData = new FormData()
    formData.append("file", filePath.files[0])
    var urlUpload = 'http://localhost:8080/api/public/upload-file';
    const res = await fetch(urlUpload, {
        method: 'POST',
        body: formData
    });
    if (res.status < 300) {
        const linkImage = await res.text();
        document.getElementById("avatar").value = linkImage;
        document.getElementById("btnchoosefile").style.backgroundImage = `url('${linkImage}')`;
    }
    document.getElementById("btn-submit").disabled = false
    document.getElementById("btn-submit").innerText = "Lưu tài liệu"
}

var listLinkResponse = [];
async function uploadTaiLieu() {
    var listLink = [];
    var listFile = [];
    var f = document.getElementById("chonnhieufile")
    for(i=0; i<f.files.length; i++){
        listFile.push(f.files[i]);
    }
    if(listFile.length == 0){
        return;
    }
    const formData = new FormData()
    for(i=0; i<listFile.length; i++){
        formData.append("file", listFile[i])
    }
    var urlUpload = 'http://localhost:8080/api/public/upload-multiple-file-name';
    const res = await fetch(urlUpload, { 
        method: 'POST', 
        body: formData
    });
    if(res.status < 300){
        var listLink = await res.json();
        listLinkResponse.push(...listLink);

        renderListFile();
    }
    console.log(listLink);

    // hiển thị ra
}

function renderListFile(){
    const tbody = document.getElementById("list-file");
    tbody.innerHTML = "";

    listLinkResponse.forEach((file, index) => {

        const isImage = file.type.startsWith("image");

        const tr = `
        <tr class="fade-row">
            <td>
                <div style="display:flex;align-items:center;gap:12px;">
                    ${isImage ? `<img src="${file.link}" style="width:50px;height:50px;object-fit:cover;border-radius:6px;">` : ``}
                    
                    <div>
                        <div style="font-weight:500">${file.oriName}</div>
                        <div style="font-size:12px;color:#888">${file.type}</div>
                    </div>
                </div>
            </td>

            <td>
                <button class="btn btn-danger btn-sm" onclick="deleteFile(${index})">
                    X
                </button>
            </td>
        </tr>
        `;

        tbody.insertAdjacentHTML("beforeend", tr);
    });
}

function renderListFileFromDetails(details){
    document.getElementById("anhdathem").style.display = ''
    const tbody = document.getElementById("list-file-details");
    tbody.innerHTML = "";

    details.forEach((file, index) => {

        const isImage = file.fileType.startsWith("image");

        const tr = `
        <tr class="fade-row" id="data-row-detail-${file.id}">
            <td>
                <div style="display:flex;align-items:center;gap:12px;">
                    ${isImage ? `<img src="${file.linkFile}" style="width:50px;height:50px;object-fit:cover;border-radius:6px;">` : ``}
                    
                    <div>
                        <div style="font-weight:500">${file.name}</div>
                        <div style="font-size:12px;color:#888">${file.fileType}</div>
                    </div>
                </div>
            </td>

            <td>
                <button class="btn btn-danger btn-sm" onclick="deleteFileDetail(${file.id})">
                    X
                </button>
            </td>
        </tr>
        `;

        tbody.insertAdjacentHTML("beforeend", tr);
    });
}

function deleteFile(index){
    listLinkResponse.splice(index,1);
    renderListFile();
}

async function deleteFileDetail(id) {
    swal({
        title: "Bạn có chắc chắn muốn xóa chi tiết tài liệu này?",
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
            var response = await deleteMethod(`/api/document/admin-teacher/delete-detail?idDetail=${id}`)
            if (response) {
                swal("Đã xóa!", "Chi tiết tài liệu đã được xóa khỏi hệ thống.", "success");
                document.getElementById(`data-row-detail-${id}`).remove();
            }
        }
    });
}