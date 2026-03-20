var size = 9;
async function loadBlog(page){
    var sort = document.getElementById("sort").value
    var search = document.getElementById("search").value
    var category = document.getElementById("category").value
    var url = `/api/blog/public/findAll?page=${page}&size=${size}&sort=${sort}`
    if(search != null && search != ''){
        url += `&search=${search}`
    }
    if(category != null && category != ''){
        url += `&category=${category}`
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
        main += `<div class="col-lg-4">
          <a class="blog-card" href="/student/blog-detail?id=${b.id}">
            <div class="blog-image">
              <img src="${b.image}">
            </div>
            <div class="blog-body">
              <span class="badge bg-primary" >${b.category.name}</span>
              <h5>${b.title}</h5>
              <p class="blog-desc">${b.description}</p>
              <div class="blog-meta">
                <span>
                  <i class="bi bi-calendar"></i>
                  <span></span>${b.createdDate}</span>
                <span> <i class="bi bi-eye"></i>
                  <span>${b.numView}</span>
                </span>
              </div>
            </div>
          </a>
        </div>`
    }
    document.getElementById("list-data").innerHTML = main
    pageable(page, totalPage, "loadBlog");
}