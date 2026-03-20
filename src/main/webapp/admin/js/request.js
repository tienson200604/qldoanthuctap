
var firstUrl = 'http://localhost:8080'
async function uploadSingleFile(filePath) {
    const formData = new FormData()
    formData.append("file", filePath.files[0])
    var urlUpload = firstUrl+'/api/public/upload-file';
    const res = await fetch(urlUpload, {
        method: 'POST',
        body: formData
    });
    if (res.status < 300) {
        var linkImage = await res.text();
        return linkImage;
    }
    else{
        return null;
    }
}

async function uploadSingleFileFormData(formData) {
    var urlUpload = firstUrl+'/api/public/upload-file';
    const res = await fetch(urlUpload, {
        method: 'POST',
        body: formData
    });
    if (res.status < 300) {
        var linkImage = await res.text();
        return linkImage;
    }
    else{
        return null;
    }
}


async function uploadMultipleFile(listFile) {
    const formData = new FormData()
    for (var i = 0; i < listFile.length; i++) {
        formData.append("file", listFile[i])
    }
    var urlUpload = firstUrl+'/api/public/upload-multiple-file';
    const res = await fetch(urlUpload, {
        method: 'POST',
        body: formData
    });
    if (res.status < 300) {
        return await res.json();
    } else {
        return [];
    }
}



var token = localStorage.getItem("token");

async function getMethod(url) {
    if(url.includes(firstUrl) == false){
        url = firstUrl + url;
    }
    const response = await fetch(url, {
        method: 'GET',
        headers: new Headers({
            'Authorization': 'Bearer ' + token
        })
    });
    if(response.status < 300){
        return response
    }
    else{
        var textMess = 'Có lỗi xảy ra khi lấy dữ liệu, mã lỗi: ' + response.status
        if(response.status == exceptionCode){
            var result = await response.json();
            textMess = result.defaultMessage;
        }
        swal({
            title: "Thông báo",
            text: textMess,
            type: "error"
        },
        function() {
        });
        return null;
    }
}

async function postMethod(url) {
    if(url.includes(firstUrl) == false){
        url = firstUrl + url;
    }
    const response = await fetch(url, {
        method: 'POST',
        headers: new Headers({
            'Authorization': 'Bearer ' + token
        })
    });
    if(response.status < 300){
        return response
    }
    else{
        var textMess = 'Có lỗi xảy ra khi gửi request, mã lỗi: ' + response.status
        if(response.status == exceptionCode){
            var result = await response.json();
            textMess = result.defaultMessage;
        }
        swal({
            title: "Thông báo",
            text: textMess,
            type: "error"
        },
        function() {
        });
        return null;
    }
}

async function postMethodTextPlan(url, payload) {
    if(url.includes(firstUrl) == false){
        url = firstUrl + url;
    }
    const response = await fetch(url, {
        method: 'POST',
        headers: new Headers({
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'text/plain'
        }),
        body: payload
    });
    if(response.status < 300){
        return response
    }
    else{
        var textMess = 'Có lỗi xảy ra khi gửi request, mã lỗi: ' + response.status
        if(response.status == exceptionCode){
            var result = await response.json();
            textMess = result.defaultMessage;
        }
        swal({
            title: "Thông báo",
            text: textMess,
            type: "error"
        },
        function() {
        });
        return null;
    }
}

async function postMethodPayload(url, payload) {
    if(url.includes(firstUrl) == false){
        url = firstUrl + url;
    }
    const response = await fetch(url, {
        method: 'POST',
        headers: new Headers({
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json'
        }),
        body: JSON.stringify(payload)
    });
    if(response.status < 300){
        return response
    }
    else{
        var textMess = 'Có lỗi xảy ra khi gửi request, mã lỗi: ' + response.status
        if(response.status == exceptionCode){
            var result = await response.json();
            textMess = result.defaultMessage;
        }
        swal({
            title: "Thông báo",
            text: textMess,
            type: "error"
        },
        function() {
        });
        return null;
    }
}

async function deleteMethod(url) {
    if(url.includes(firstUrl) == false){
        url = firstUrl + url;
    }
    const response = await fetch(url, {
        method: 'DELETE',
        headers: new Headers({
            'Authorization': 'Bearer ' + token
        })
    });
    if(response.status < 300){
        return response
    }
    else{
        var textMess = 'Có lỗi xảy ra khi gửi request, mã lỗi: ' + response.status
        if(response.status == exceptionCode){
            var result = await response.json();
            textMess = result.defaultMessage;
        }
        swal({
            title: "Thông báo",
            text: textMess,
            type: "error"
        },
        function() {
        });
        return null;
    }
}


function pageable(page, totalPage, callback){
    let mainpage = '';

    let start = Math.max(0, page - 2);
    let end = Math.min(totalPage - 1, page + 2);

    // nút previous
    if(page > 0){
        mainpage += `<li class="page-item">
            <a class="page-link" href="#" onclick="${callback}(${page-1})">«</a>
        </li>`;
    }

    // nếu bị cắt đầu
    if(start > 0){
        mainpage += `<li class="page-item">
            <a class="page-link" href="#" onclick="${callback}(0)">1</a>
        </li>`;

        if(start > 1){
            mainpage += `<li class="page-item disabled"><a class="page-link">...</a></li>`;
        }
    }

    // các trang chính
    for(let i = start; i <= end; i++){
        mainpage += `<li class="page-item ${page == i ? 'active' : ''}">
            <a class="page-link" href="#" onclick="${callback}(${i})">${i+1}</a>
        </li>`;
    }

    // nếu bị cắt cuối
    if(end < totalPage - 1){
        if(end < totalPage - 2){
            mainpage += `<li class="page-item disabled"><a class="page-link">...</a></li>`;
        }

        mainpage += `<li class="page-item">
            <a class="page-link" href="#" onclick="${callback}(${totalPage-1})">${totalPage}</a>
        </li>`;
    }

    // nút next
    if(page < totalPage - 1){
        mainpage += `<li class="page-item">
            <a class="page-link" href="#" onclick="${callback}(${page+1})">»</a>
        </li>`;
    }

    document.getElementById("pagination").innerHTML = mainpage;
}