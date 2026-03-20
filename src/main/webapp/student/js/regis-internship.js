function selectType(type){

    var taiTruong = document.getElementById("form-area-taitruong");
    var ctyLienKet = document.getElementById("form-area-cty-lienket");
    var ctyNgoai = document.getElementById("form-area-cty-ngoai");

    document.querySelectorAll(".internship-card")
        .forEach(e => e.classList.remove("active"));

    event.currentTarget.classList.add("active");

    if(type === "TAI_TRUONG"){
        taiTruong.classList.remove("d-none");
        ctyLienKet.classList.add("d-none");
        ctyNgoai.classList.add("d-none");
    }
    else if(type === "CTY_LIEN_KET"){
        ctyLienKet.classList.remove("d-none");
        taiTruong.classList.add("d-none");
        ctyNgoai.classList.add("d-none");
    }
    else if(type === "DOANH_NGHIEP_NGOAI"){
        ctyNgoai.classList.remove("d-none");
        ctyLienKet.classList.add("d-none");
        taiTruong.classList.add("d-none");
    }

}

function selectTeacherTaiTruong(card){
    if(card.classList.contains("disabled-card")){
        return;
    }
    document.querySelectorAll(".teacher-card")
        .forEach(c => c.classList.remove("active"));
    card.classList.add("active");
    var id = card.getAttribute("data-id");
    document.getElementById("semesterTeacherIdTaiTruong").value = id;
    document.getElementById("semesterTeacherIdLienKet").value = '';
    document.getElementById("semesterTeacherIdNgoai").value = '';
}

function selectTeacherLienKet(card){
    if(card.classList.contains("disabled-card")){
        return;
    }
    document.querySelectorAll(".teacher-card")
        .forEach(c => c.classList.remove("active"));
    card.classList.add("active");
    var id = card.getAttribute("data-id");
    document.getElementById("semesterTeacherIdLienKet").value = id;
    document.getElementById("semesterTeacherIdTaiTruong").value = '';
    document.getElementById("semesterTeacherIdNgoai").value = '';
}


function selectTeacherNgoai(card){
    if(card.classList.contains("disabled-card")){
        return;
    }
    document.querySelectorAll(".teacher-card")
        .forEach(c => c.classList.remove("active"));
    card.classList.add("active");
    var id = card.getAttribute("data-id");
    document.getElementById("semesterTeacherIdNgoai").value = id;
    document.getElementById("semesterTeacherIdTaiTruong").value = '';
    document.getElementById("semesterTeacherIdLienKet").value = '';
}

function selectCompany(card){
    document.querySelectorAll(".company-card")
        .forEach(c => c.classList.remove("active"));
    card.classList.add("active");
    var id = card.getAttribute("data-id");
    document.getElementById("companyId").value = id;
}

async function regisAction(type){
    var payload = {
        semesterTeacherId:null,
        semesterCompanyId:null,
        companyName:document.getElementById("companyName").value,
        companyAddress:document.getElementById("companyAddress").value,
        companyPhone:document.getElementById("phoneNumber").value,
        companyEmail:document.getElementById("companyEmail").value,
        taxCode:document.getElementById("taxCode").value,
        introductionPaper:document.getElementById("giaygioithieu").value,
        internshipType:type,
    }
    if(type === "TAI_TRUONG"){
        payload.semesterTeacherId = document.getElementById("semesterTeacherIdTaiTruong").value
        if(payload.semesterTeacherId == null || payload.semesterTeacherId == ''){
            swal('Thông báo','Hãy chọn giảng viên hướng dẫn','error'); return;
        }
    }
    else if(type === "DOANH_NGHIEP_LIEN_KET"){
        payload.semesterTeacherId = document.getElementById("semesterTeacherIdLienKet").value
        if(payload.semesterTeacherId == null || payload.semesterTeacherId == ''){
            swal('Thông báo','Hãy chọn giảng viên hướng dẫn','error'); return;
        }
        payload.semesterCompanyId = document.getElementById("companyId").value;
        if(payload.semesterCompanyId == null || payload.semesterCompanyId == ''){
            swal('Thông báo','Hãy chọn công ty liên kết','error'); return;
        }
    }
    else if(type === "DOANH_NGHIEP_NGOAI"){
        payload.semesterTeacherId = document.getElementById("semesterTeacherIdNgoai").value
        if(payload.semesterTeacherId == null || payload.semesterTeacherId == ''){
            swal('Thông báo','Hãy chọn giảng viên hướng dẫn','error'); return;
        }
        if(payload.companyName == '' || payload.companyName == null){
            swal('Thông báo','Hãy nhập tên công ty','error'); return;
        }
        if(payload.companyAddress == '' || payload.companyAddress == null){
            swal('Thông báo','Hãy nhập địa chỉ công ty','error'); return;
        }
    }
    var response = await postMethodPayload('/api/student-regis/student/create', payload);
    if(response.status < 300){
        swal('Chúc mừng','Bạn đã đăng ký thực tập thành công','success');
    }
}

async function uploadGiayGioiThieu(){
    document.getElementById("btn-submit-dn-ngoai").disabled = true
    document.getElementById("btn-submit-dn-ngoai").innerText = "Đang tải file..."
    const filePath = document.getElementById('changeGiayGioiThieu')
    const formData = new FormData()
    formData.append("file", filePath.files[0])
    var urlUpload = 'http://localhost:8080/api/public/upload-file';
    const res = await fetch(urlUpload, {
        method: 'POST',
        body: formData
    });
    if (res.status < 300) {
        const linkImage = await res.text();
        document.getElementById("giaygioithieu").value = linkImage;
        document.getElementById("thongtingiaygioithieu").classList.remove('d-none');
        // document.getElementById("imgpreview").src = linkImage
    }
    document.getElementById("btn-submit-dn-ngoai").disabled = false
    document.getElementById("btn-submit-dn-ngoai").innerText = "Đăng ký thực tập"
}