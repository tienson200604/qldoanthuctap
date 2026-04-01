$(document).ready(function() {
    loadAllRates();
});

async function loadAllRates() {
    const res = await getMethod('/api/rate/admin/all');
    if (res != null) {
        const rates = await res.json();
        let html = '';
        for (let i = 0; i < rates.length; i++) {
            const r = rates[i];
            const teacherName = r.studentRegis?.semesterTeacher?.teacher?.fullname || 'N/A';
            const projectName = r.studentRegis?.semesterTeacher?.projectName || 'N/A';
            const avg = ((r.q1 + r.q2 + r.q3) / 3).toFixed(1);

            html += `<tr>
                <td>${i + 1}</td>
                <td class="fw-semibold">${teacherName}</td>
                <td>${projectName}</td>
                <td class="text-center">
                    <span class="badge bg-info-subtle text-info border border-info-subtle px-2">${r.q1} / 5</span>
                </td>
                <td class="text-center">
                    <span class="badge bg-success-subtle text-success border border-success-subtle px-2">${r.q2} / 5</span>
                </td>
                <td class="text-center">
                    <span class="badge bg-warning-subtle text-warning border border-warning-subtle px-2">${r.q3} / 5</span>
                </td>
                <td class="text-center text-primary fw-bold fs-5">${avg}</td>
                <td>
                    <div class="bg-light p-2 rounded small border-start border-4 border-primary">
                        ${r.comment ? r.comment : '<i class="text-muted">Không có nhận xét</i>'}
                    </div>
                </td>
            </tr>`;
        }
        document.getElementById('rateTableBody').innerHTML = html;
        // Initialize DataTable after data is loaded
        $('#rateTable').DataTable({
            "language": {
                "url": "//cdn.datatables.net/plug-ins/1.13.2/i18n/vi.json"
            }
        });
    }
}
