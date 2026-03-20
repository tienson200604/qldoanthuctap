async function loadNamHoc() {
    var response = await getMethod(`/api/semester/public/all`)
    var list = await response.json();
    var main = '';
    for (i = 0; i < list.length; i++) {
        main += `<option value="${list[i].id}">Năm học: ${list[i].yearName}</option>`
    }
    document.getElementById("semester").innerHTML = main
    $('#semester').select2({
        theme: "bootstrap-5",
        width: '100%', 
        templateSelection: (data) => data.text 
    });
}

async function loadStatus() {
    var response = await getMethod(`/api/semester-type/admin/type`)
    var list = await response.json();
    renderStatusButtons(list);
}

async function loadTeacher() {
    var response = await getMethod(`/api/user/admin/get-all-teacher`)
    var list = await response.json();
    //xóa các user trong list nếu user này nằm trong listTeacherDaTonTai
    list = list.filter(user => 
        !listTeacherDaTonTai.some(t => t.teacher.id == user.id)
    );
    listTeacher = list;
    var main = '';
    for (i = 0; i < list.length; i++) {
        main += `<option value="${list[i].id}">userId:${list[i].id} - ${list[i].fullname} - ${list[i].email}</option>`
    }
    document.getElementById("teachers").innerHTML = main
    $('#teachers').select2({
        theme: "bootstrap-5",
        width: '100%', 
        templateSelection: (data) => data.text 
    });
}

async function loadSemesterType() {
    var namhoc = document.getElementById("semester").value
    var response = await getMethod(`/api/semester-type/public/find-all?semesterId=${namhoc}`)
    if(response == null){
        document.getElementById("list-data").innerHTML = '<tr><td colspan="6" class="text-center">Lỗi khi tải dữ liệu</td></tr>';
    }
    var list = await response.json();
    if(list.length == 0){
        document.getElementById("list-data").innerHTML = '<tr><td colspan="6" class="text-center">Không có dữ liệu</td></tr>';
        return;
    }
    var main = '';
    for (i = 0; i < list.length; i++) {
        main += ` <tr id="data-row-${list[i].id}">
                    <td>${list[i].id}</td>
                    <td>${list[i].type.displayName}</td>
                    <td>${list[i].semester.yearName}</td>
                    <td>${list[i].createdDate}</td>
                    <td>${list[i].updateDate}</td>
                    <td>${list[i].deadlineRegis}</td>
                    <td class="sticky-col">
                        <button onclick="deleteData(${list[i].id})" class="btn btn-sm btn-danger"><i class="bi bi-trash"></i></button>
                        <a href="add-semester-type?id=${list[i].id}" class="btn btn-sm btn-primary"><i class="bi bi-pencil"></i></a>
                    </td>
                </tr>`
    }
    document.getElementById("list-data").innerHTML = main
}

async function saveSemesterType() {
    var uls = new URL(document.URL)
    var id = uls.searchParams.get("id");
    var payload = {
        "id":id,
        "semesterId": document.getElementById("semester").value,
        "type": document.getElementById("statusValue").value,
        "deadlineRegis": document.getElementById("deadlineRegis").value,
        "semesterTeacherRequests": []
    }
    var listIdTeacher = $("#teachers").val();
    for (let i = 0; i < listIdTeacher.length; i++) {
        let id = listIdTeacher[i];

        let max = document.getElementById("soluongtoida-" + id).value;
        let tendetai = document.getElementById("tendetai-" + id).value;
        let motadetai = document.getElementById("motadetai-" + id).value;

        // kiểm tra số lượng
        if (max == "" || isNaN(max) || Number(max) <= 0) {
            swal("Thông báo","Số lượng sinh viên tối đa của user" + id + " không hợp lệ!",'error');
            document.getElementById("soluongtoida-" + id).focus();
            return;
        }

        payload.semesterTeacherRequests.push({
            "maxStudents": Number(max),
            "teacherId": id,
            "projectName": tendetai,
            "descriptionProject": motadetai,
        })
    }

    for(i=0; i< listTeacherDaTonTai.length; i++){
        var s = listTeacherDaTonTai[i];
        payload.semesterTeacherRequests.push({
            "id":s.id,
            "maxStudents":document.getElementById("soluongtoidaupdate-"+s.id).value,
            "teacherId":s.teacher.id,
            "projectName":document.getElementById("tendetaiupdate-"+s.id).value,
            "descriptionProject":document.getElementById("motadetaiupdate-"+s.id).value,
        })
    }
    var response = await postMethodPayload(`/api/semester-type/admin/create-update`, payload)
    if (response != null) {
        swal({
            title: "Thông báo",
            text: "Lưu loại đề tài cho năm học thành công",
            type: "success"
        },
        function() {
            window.location.href = 'semester-type'
        });
    }
}

async function loadASemesterType() {
    var id = window.location.search.split('=')[1];
    if (id != null) {
        var response = await getMethod(`/api/semester-type/public/find-by-id?id=${id}`)
        if(response == null){
            return;
        }
        var result = await response.json();
        document.getElementById("statusValue").value = result.type.name
        document.getElementById("deadlineRegis").value = result.deadlineRegis
        document.getElementById(`status-${result.type.name}`).click();
        $("#semester").val(result.semester.id)

        var res = await getMethod(`/api/semester-teacher/admin/find-by-type?semesterTypeId=${id}`)
        if(res == null){
            return;
        }
        var list = await res.json();
        listTeacherDaTonTai = list;
        changeTeacherUpdate(list)
        // var listIdUser = list.map(x => x.id);
        // $("#teachers").val(listIdUser).change();
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
            var response = await deleteMethod(`/api/semester-type/admin/delete?id=${id}`)
            if (response) {
                swal("Đã xóa!", "Loại thực tập đã được xóa khỏi hệ thống.", "success");
                document.getElementById(`data-row-${id}`).remove();
            }
        }
    });
}

function renderStatusButtons(list){

    const container = document.getElementById("statusButtons");
    container.innerHTML = "";

    list.forEach(item => {

        const btn = document.createElement("button");
        btn.className = "status-btn";
        btn.id = `status-${item.name}`;
        btn.innerText = item.displayName;

        btn.style.borderColor = item.color;
        btn.style.color = item.color;

        btn.title = item.description;

        btn.onclick = function(){

            document.querySelectorAll(".status-btn").forEach(b=>{
                b.classList.remove("active");
                b.style.background = "white";
                b.style.color = b.style.borderColor;
            });

            btn.classList.add("active");
            btn.style.background = item.color;
            btn.style.color = "white";

            document.getElementById("statusValue").value = item.name;
        }

        container.appendChild(btn);
    });
}

var listTeacherDaTonTai = [];
var listTeacher = [];
var teacherData = {}

async function changeTeacher() {

    // Lưu dữ liệu đã nhập trước khi render lại
    document.querySelectorAll("#list-giangvien tr").forEach(tr=>{
        var hidden = tr.querySelector("input[type='hidden']");
        if(!hidden) return;

        var id = hidden.id.replace("iduser-","");

        teacherData[id] = {
            name: document.getElementById("tendetai-"+id)?.value || "",
            max: document.getElementById("soluongtoida-"+id)?.value || "",
            desc: document.getElementById("motadetai-"+id)?.value || ""
        }
    });

    var tbody = document.getElementById("list-giangvien");
    var teacherIdSelect = $("#teachers").val();
    var teacherSelect = [];

    if(teacherIdSelect && teacherIdSelect.length > 0){
        teacherSelect = listTeacher.filter(t => teacherIdSelect.includes(String(t.id)));
    }

    var main = '';

    for(var i=0; i<teacherSelect.length; i++){

        var t = teacherSelect[i];

        var ten = teacherData[t.id]?.name || "";
        var mota = teacherData[t.id]?.desc || "";
        var max = teacherData[t.id]?.max || "";

        main += `
        <tr>
            <td style="max-width:200px;">
                <div class="row">
                    ${!t.avatar ? '' :
                    `<div class="col-sm-4">
                        <img style="width:100%; border-radius:8px" src="${t.avatar}">
                    </div>`}

                    <div ${!t.avatar ? 'class="col-sm-12"' : 'class="col-sm-8"'}>
                        User Id: ${t.id}<br>
                        Họ tên: ${t.fullname}<br>
                        Email: ${t.email}<br>
                        Số điện thoại: ${t.phone}<br>
                    </div>
                </div>
            </td>

            <td style="max-width:200px;">
                <input id="soluongtoida-${t.id}" value="${max}" class="form-control">
            </td>

            <td style="max-width:200px;">
                <input type="hidden" id="iduser-${t.id}">
                <input id="tendetai-${t.id}" value="${ten}" class="form-control">
            </td>

            <td>
                <textarea class="form-control" id="motadetai-${t.id}">${mota}</textarea>
            </td>

            <td style="max-width:100px;">
                <button class="btn btn-danger" onclick="removeTeacher(${t.id})">X</button>
            </td>
        </tr>`;
    }

    tbody.innerHTML = main;
}

function removeTeacher(id){

    var select = $("#teachers").val() || [];

    select = select.filter(x => x != id);

    $("#teachers").val(select).trigger("change");
}

async function changeTeacherUpdate(list) {
    var tbody = document.getElementById("list-giangvien-update");
    var main = '';
    for(i=0; i< list.length; i++){
        var t = list[i];
        main += 
        `<tr id="data-row-${t.id}">
            <td style="max-width:200px;">
                <div class="row">
                    ${!t.teacher.avatar ? '' :
                    `<div class="col-sm-4">
                        <img style="width:100%; border-radius:8px" src="${t.avatar}">
                    </div>`}

                    <div ${!t.teacher.avatar ? 'class="col-sm-12"' : 'class="col-sm-8"'}>
                        User Id: ${t.teacher.id}<br>
                        Họ tên: ${t.teacher.fullname}<br>
                        Email: ${t.teacher.email}<br>
                        Số điện thoại: ${t.teacher.phone}<br>
                    </div>
                </div>
            </td>

            <td style="max-width:200px;">
                <input id="soluongtoidaupdate-${t.id}" value="${t.maxStudents}" class="form-control">
            </td>

            <td style="max-width:200px;">
                <input type="hidden" id="idupdate-${t.id}">
                <input id="tendetaiupdate-${t.id}" value="${t.projectName}" class="form-control">
            </td>

            <td>
                <textarea class="form-control" id="motadetaiupdate-${t.id}">${t.descriptionProject}</textarea>
            </td>

            <td style="max-width:100px;">
                <button class="btn btn-danger" onclick="removeTeacherUpdate(${t.id})">X</button>
            </td>
        </tr>`
    }
    tbody.innerHTML = main;
}

async function removeTeacherUpdate(id) {
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
            var response = await deleteMethod(`/api/semester-teacher/admin/delete?id=${id}`)
            if (response) {
                swal("Đã xóa!", "Giảng viên trong năm học này đã được xóa khỏi hệ thống.", "success");
                document.getElementById(`data-row-${id}`).remove();
            }
        }
    });
}