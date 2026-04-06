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

function getTrimmedValue(id){
    return document.getElementById(id).value.trim();
}

function isValidEmail(value){
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
}

function isValidPhoneNumber(value){
    return /^0\d{9,10}$/.test(value);
}

function isValidTaxCode(value){
    return /^(\d{10}|\d{13})$/.test(value);
}

function validateExternalCompanyForm(payload){
    if(payload.companyName === ''){
        return 'Hãy nhập tên công ty';
    }
    if(payload.companyName.length < 3){
        return 'Tên công ty phải có ít nhất 3 ký tự';
    }
    if(payload.taxCode === ''){
        return 'Hãy nhập mã số thuế';
    }
    if(!isValidTaxCode(payload.taxCode)){
        return 'Mã số thuế phải gồm 10 hoặc 13 chữ số';
    }
    if(payload.companyEmail === ''){
        return 'Hãy nhập email công ty';
    }
    if(!isValidEmail(payload.companyEmail)){
        return 'Email công ty không hợp lệ';
    }
    if(payload.companyPhone === ''){
        return 'Hãy nhập số điện thoại công ty';
    }
    if(!isValidPhoneNumber(payload.companyPhone)){
        return 'Số điện thoại phải bắt đầu bằng số 0 và có 10-11 chữ số';
    }
    if(payload.companyAddress === ''){
        return 'Hãy nhập địa chỉ công ty';
    }
    if(payload.companyAddress.length < 10){
        return 'Địa chỉ công ty phải có ít nhất 10 ký tự';
    }
    if(payload.introductionPaper === '' || payload.introductionPaper == null){
        return 'Hãy upload giấy giới thiệu';
    }
    return null;
}

async function regisAction(type){
    var payload = {
        semesterTeacherId:null,
        semesterCompanyId:null,
        companyName:getTrimmedValue("companyName"),
        companyAddress:getTrimmedValue("companyAddress"),
        companyPhone:getTrimmedValue("phoneNumber"),
        companyEmail:getTrimmedValue("companyEmail"),
        taxCode:getTrimmedValue("taxCode"),
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
        var validateMessage = validateExternalCompanyForm(payload);
        if(validateMessage != null){
            swal('Thông báo', validateMessage,'error'); return;
        }
    }
    var response = await postMethodPayload('/api/student-regis/student/create', payload);
    if(response.status < 300){
        if(type === "DOANH_NGHIEP_NGOAI"){
            swal('Chúc mừng','Bạn đã đăng ký thực tập thành công, vui lòng chờ giảng viên duyệt','success');
        } else {
            swal('Chúc mừng','Bạn đã đăng ký thực tập thành công','success');
        }
    }
}

async function uploadGiayGioiThieu(){
    const fileInput = document.getElementById('changeGiayGioiThieu');
    const file = fileInput.files[0];
    if(!file){
        return;
    }
    var allowedExtensions = ['pdf', 'doc', 'docx'];
    var extension = file.name.split('.').pop().toLowerCase();
    if(!allowedExtensions.includes(extension)){
        swal('Thông báo','Chỉ chấp nhận file PDF, DOC hoặc DOCX','error');
        fileInput.value = '';
        return;
    }
    if(file.size > 10 * 1024 * 1024){
        swal('Thông báo','Dung lượng giấy giới thiệu không được vượt quá 10MB','error');
        fileInput.value = '';
        return;
    }
    document.getElementById("btn-submit-dn-ngoai").disabled = true
    document.getElementById("btn-submit-dn-ngoai").innerText = "Đang tải file..."
    const formData = new FormData()
    formData.append("file", file)
    var urlUpload = '/api/public/upload-file';
    const res = await fetch(urlUpload, {
        method: 'POST',
        body: formData
    });
    if (res.status < 300) {
        const linkImage = await res.text();
        document.getElementById("giaygioithieu").value = linkImage;
        document.getElementById("thongtingiaygioithieu").classList.remove('d-none');
        // document.getElementById("imgpreview").src = linkImage
    } else {
        swal('Thông báo','Upload giấy giới thiệu thất bại','error');
        document.getElementById("giaygioithieu").value = '';
        document.getElementById("thongtingiaygioithieu").classList.add('d-none');
    }
    document.getElementById("btn-submit-dn-ngoai").disabled = false
    document.getElementById("btn-submit-dn-ngoai").innerText = "Đăng ký thực tập"
}
