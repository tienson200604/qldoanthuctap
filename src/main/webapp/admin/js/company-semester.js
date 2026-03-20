async function loadNamHoc() {
    var response = await getMethod(`/api/semester/public/all`)
    var list = await response.json();
    var main = '';
    for (i = 0; i < list.length; i++) {
        main += `<option value="${list[i].id}">Năm học: ${list[i].yearName}</option>`
    }
    document.getElementById("semester").innerHTML = main
    document.getElementById("semester-add").innerHTML = main
    $('#semester').select2({
        theme: "bootstrap-5",
        width: '100%', 
        templateSelection: (data) => data.text 
    });
    $('#semester-add').select2({
        theme: "bootstrap-5",
        width: '100%', 
        templateSelection: (data) => data.text 
    });
}


async function loadCompanys() {
    var response = await getMethod(`/api/company/public/find-all-list`)
    var list = await response.json();
    listCompany = list;
    var main = '';
    for (i = 0; i < list.length; i++) {
        main += `<option value="${list[i].id}">Id: ${list[i].id} - ${list[i].name} - ${list[i].taxCode}</option>`
    }
    document.getElementById("company").innerHTML = main
    $('#company').select2({
        theme: "bootstrap-5",
        width: '100%', 
        templateSelection: (data) => data.text 
    });
}

var size = 10;
async function loadCongTyNamHoc(page) {
    var namhoc = document.getElementById("semester").value
    var url = `/api/semester-company/public/find-all?size=${size}&page=${page}&semesterId=${namhoc}`
    var search = document.getElementById("search").value
    if(search != null && search != ''){
        url += `&search=${search}`
    }
    var response = await getMethod(url)
    if(response == null){
        document.getElementById("list-data").innerHTML = '<tr><td colspan="6" class="text-center">Lỗi khi tải dữ liệu</td></tr>';
    }
    var result = await response.json();
    var list = result.content;
    var totalPage = result.totalPages;
    if(list.length == 0){
        document.getElementById("list-data").innerHTML = '<tr><td colspan="6" class="text-center">Không có dữ liệu</td></tr>';
        return;
    }
    var main = '';
    for (i = 0; i < list.length; i++) {
        main += ` <tr id="data-row-${list[i].id}">
                    <td>${list[i].id}</td>
                    <td>${list[i].company.name}</td>
                    <td>${list[i].company.address}</td>
                    <td>${list[i].company.email}</td>
                    <td>${list[i].maxStudent}</td>
                    <td class="sticky-col">
                        <button onclick="deleteData(${list[i].id})" class="btn btn-sm btn-danger"><i class="bi bi-trash"></i></button>
                        <button onclick="loadDetail(${list[i].id})" data-bs-toggle="modal" data-bs-target="#editmodal" class="btn btn-sm btn-primary"><i class="bi bi-pencil"></i></button>
                    </td>
                </tr>`
    }
    document.getElementById("list-data").innerHTML = main
     pageable(page, totalPage, "loadCongTyNamHoc");
}

var listCompany = []
var companyData = {};
async function changeCompany() {

    // lưu dữ liệu hiện tại trước khi render lại
    document.querySelectorAll("#list-select-company tr").forEach(tr=>{
        let id = tr.querySelector("input[id^='id-company-']").value;

        companyData[id] = {
            sl: document.getElementById("sl-sinhvien-"+id)?.value || "",
            mota: document.getElementById("mota-"+id)?.value || ""
        }
    });

    var listIdCompany = $("#company").val();
    var listCompanySelect = [];

    for (let i = 0; i < listCompany.length; i++) {
        if (listIdCompany.includes(String(listCompany[i].id))) {
            listCompanySelect.push(listCompany[i]);
        }
    }

    var tbody = document.getElementById("list-select-company");
    var main = '';

    for(let i=0; i< listCompanySelect.length; i++){

        let id = listCompanySelect[i].id;

        let sl = companyData[id]?.sl || "";
        let mota = companyData[id]?.mota || "";

        main += `
        <tr>
            <td>
                <input readonly id="id-company-${id}" value="${id}" 
                style="width:60px" class="form-control">
            </td>

            <td>${listCompanySelect[i].name}</td>

            <td>
                <input id="sl-sinhvien-${id}" value="${sl}" 
                class="form-control" style="width:120px;">
            </td>

            <td>
                <textarea id="mota-${id}" 
                class="form-control">${mota}</textarea>
            </td>
        </tr>`;
    }

    tbody.innerHTML = main;
}

async function saveCompanySemester() {
    var payload = {
        "semesterId": document.getElementById("semester-add").value,
        "semesterCompanyInserts": []
    }
    var listIdCompany = $("#company").val();
    for (let i = 0; i < listIdCompany.length; i++) {

        let id = listIdCompany[i];
        let sl = document.getElementById("sl-sinhvien-" + id).value;

        // kiểm tra số lượng
        if (sl == "" || isNaN(sl) || Number(sl) <= 0) {
            swal("Thông báo","Số lượng sinh viên của công ty ID " + id + " không hợp lệ!",'error');
            document.getElementById("sl-sinhvien-" + id).focus();
            return;
        }

        payload.semesterCompanyInserts.push({
            "maxStudent": Number(sl),
            "companyId": id,
            "description": document.getElementById("mota-" + id).value,
        })
    }
    var response = await postMethodPayload(`/api/semester-company/admin/create`, payload)
    if (response != null) {
        swal({
            title: "Thông báo",
            text: "Lưu công ty cho năm học thành công",
            type: "success"
        },
        function() {
            window.location.reload();
        });
    }
}

async function deleteData(id) {
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
            var response = await deleteMethod(`/api/semester-company/admin/delete?id=${id}`)
            if (response) {
                swal("Đã xóa!", "Công ty trong năm học đã được xóa khỏi hệ thống.", "success");
                document.getElementById(`data-row-${id}`).remove();
            }
        }
    });
}

async function loadDetail(id) {
    var response = await getMethod(`/api/semester-company/public/find-by-id?id=${id}`)
    var result = await response.json();
    document.getElementById("idupdate").value = result.id
    document.getElementById("soluongupdate").value = result.maxStudent
    document.getElementById("motaupdate").value = result.description
}

async function updateCompanySemester() {
    var payload = {
        "id": document.getElementById("idupdate").value,
        "maxStudent": document.getElementById("soluongupdate").value,
        "description": document.getElementById("motaupdate").value,
    }
    var response = await postMethodPayload(`/api/semester-company/admin/update`, payload)
    if (response != null) {
        swal({
            title: "Thông báo",
            text: "Lưu công ty cho năm học thành công",
            type: "success"
        },
        function() {
            loadCongTyNamHoc(0);
            $("#editmodal").modal("hide")
        });
    }
}