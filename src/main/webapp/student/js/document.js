var size = 9;
async function loadDocument(page){
    var sort = document.getElementById("sort").value
    var search = document.getElementById("search").value
    var category = document.getElementById("category").value
    var url = `/api/document/public/search-public?page=${page}&size=${size}&sort=${sort}`
    if(search != null && search != ''){
        url += `&search=${search}`
    }
    if(category != null && category != ''){
        url += `&categoryId=${category}`
    }
    var response = await getMethod(url)
    if(response == null){
        return;
    }
    var result = await response.json();
    var totalPage = result.totalPages
    var list = result.content;
    if(list.length == 0){

    }
    var main = '';
    for (i = 0; i < list.length; i++) {
        var b = list[i]
        main += `<div class="col-lg-3 col-md-4 col-sm-6">
      <div class="card document-card border-0 shadow-sm h-100">

        <img src="${b.linkImage != null && b.linkImage != ''?b.linkImage:'/image/doc-default.jpg'}"
             class="card-img-top document-image">

        <div class="card-body d-flex flex-column">

          <h6 class="fw-bold mb-2">
            ${b.name}
          </h6>

          <p class="text-muted small flex-grow-1">
            <span class="badge bg-primary">${b.category.name}</span>
          </p>

          <div class="small text-muted mb-2">
            <i class="bi bi-eye"></i> ${b.numberView} view
            <span class="mx-2">•</span>
            <i class="bi bi-download"></i> ${b.numberDownload ? b.numberDownload : 0} download
          </div>

          <div class="small text-muted mb-3">
            <i class="fa fa-calendar"></i> ${b.createdDate}
          </div>

          <div class="d-grid">
            <a href="/student/document-detail?id=${b.id}" class="btn btn-outline-primary btn-sm">
              <i class="bi bi-download"></i>
              Xem tài liệu
            </a>
          </div>

        </div>

      </div>
    </div>`
    }
    document.getElementById("list-data").innerHTML = main
    pageable(page, totalPage, "loadDocument");
}

async function upDownload(){
    var uls = new URL(document.URL)
    var id = uls.searchParams.get("id");
    var response = await postMethod('/api/document/public/up-download?id='+id)
}

