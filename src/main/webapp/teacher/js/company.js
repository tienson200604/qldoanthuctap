var size = 9;
var listCompany = [];
async function loadCompany(page){
    var search = document.getElementById("search").value
    var semester = document.getElementById("semester").value
    var url = `/api/semester-company/public/find-all?page=${page}&size=${size}`
    if(search != null && search != ''){
        url += `&search=${search}`
    }
    if(semester != null && semester != ''){
        url += `&semesterId=${semester}`
    }
    var response = await getMethod(url)
    if(response == null){
        return;
    }
    var result = await response.json();
    var totalPage = result.totalPages
    var totalElements = result.totalElements
    document.getElementById("totalElements").innerHTML = totalElements
    var list = result.content;
    listCompany = list
    if(list.length == 0){
    }
    var main = '';
    for (i = 0; i < list.length; i++) {
        var b = list[i]
        main += `<div class="col-lg-6">
      <div class="card company-card border-0 shadow-sm">
        <div class="company-banner">
          <img src="${b.company.imageBanner}">
          <div class="company-overlay"></div>
          ${b.semester.isActive == true?`<div class="company-status badge bg-success">Đang nhận thực tập</div>`:'<div class="company-status badge bg-secondary">Đã kết thúc</div>'}
        </div>
        <div class="card-body">
          <div class="company-header">
            <div class="company-logo">
              <i class="bi bi-building"></i>
            </div>
            <div class="company-info">
              <h5 class="company-name">${b.company.name}</h5>
              <div class="company-address">
                <i class="bi bi-geo-alt"></i>${b.company.address}
              </div>
            </div>
          </div>
          <p class="company-desc">
            Liên kết ngày: ${b.company.createdDate} - Mã số thuế: ${b.company.taxCode}
          </p>
          <div class="company-stats">
            <div class="stat-box">
              <div class="stat-number text-primary">${b.maxStudent}</div>
              <div class="stat-label">Chỉ tiêu</div>
            </div>
            <div class="stat-box">
              <div class="stat-number text-success">${b.currentStudent}</div>
              <div class="stat-label">Đã đăng ký</div>
            </div>

            <div class="stat-box">
              <div class="stat-number text-danger">${Number(b.maxStudent) - Number(b.currentStudent)}</div>
              <div class="stat-label">Còn lại</div>
            </div>
          </div>
          <div class="company-contact">
            <div>
              <i class="bi bi-telephone"></i> ${b.company.phone}
            </div>
            <div>
              <i class="bi bi-envelope"></i> ${b.company.email}
            </div>
          </div>
          <div class="company-actions">
            <a href="${b.company.website}" target="_blank" class="btn btn-outline-primary btn-sm">
              <i class="bi bi-globe"></i>
              Website
            </a>
            <a onclick="setDescription(${b.id})" href="#" class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#exampleModal">
              <i class="bi bi-eye"></i>
              Xem chi tiết
            </a>
          </div>
        </div>
      </div>
    </div>`
    }
    document.getElementById("list-data").innerHTML = main
    pageable(page, totalPage, "loadCompany");
}

function setDescription(id){
    var obj = null;
    for(i=0;  i<listCompany.length; i++){
        if(listCompany[i].id == id){
            obj = listCompany[i];
            break;
        }
    }
    document.getElementById("description-company").innerHTML = obj.company.description
    document.getElementById("company-name").innerHTML = obj.company.name
}