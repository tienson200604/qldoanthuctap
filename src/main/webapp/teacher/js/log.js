var size = 10;

async function loadMyLogs(page) {
    var search = document.getElementById("search").value.trim();
    var logLevel = document.getElementById("logLevel").value === "" ? null : document.getElementById("logLevel").value;
    let dateRange = $('#dateRange').val();
    let from = null, to = null;

    if (dateRange) {
        let dates = dateRange.split(' - ');
        from = moment(dates[0], 'DD/MM/YYYY').startOf('day').format('YYYY-MM-DDTHH:mm:ss');
        to = moment(dates[1], 'DD/MM/YYYY').endOf('day').format('YYYY-MM-DDTHH:mm:ss');
    }

    var url = '/api/logs/all/my-logs?page=' + page + '&size=' + size;
    if (search !== '') {
        url += '&keyword=' + encodeURIComponent(search);
    }
    if (logLevel) {
        url += '&logLevel=' + logLevel;
    }
    if (from && to) {
        url += '&from=' + from + '&to=' + to;
    }

    var response = await getMethod(url);
    if (response == null) {
        return;
    }
    var result = await response.json();
    renderMyLogTable(result.content || []);
    pageable(page, result.totalPages, "loadMyLogs");
}

function renderMyLogTable(list) {
    if (list.length === 0) {
        document.getElementById("logTableBody").innerHTML = '<tr><td colspan="3" class="text-center py-4">Chưa có nhật ký hoạt động</td></tr>';
        document.getElementById("pagination").innerHTML = '';
        return;
    }

    var main = '';
    for (var i = 0; i < list.length; i++) {
        var createdDate = new Date(list[i].createdDate).toLocaleString('vi-VN');
        var badgeClass = list[i].logLevel === 'ERROR' ? 'danger' : (list[i].logLevel === 'WARNING' ? 'warning text-dark' : 'info');
        main += `<tr>
                    <td>${createdDate}</td>
                    <td>${list[i].actionContent}</td>
                    <td><span class="badge bg-${badgeClass}">${list[i].logLevel}</span></td>
                </tr>`;
    }
    document.getElementById("logTableBody").innerHTML = main;
}
