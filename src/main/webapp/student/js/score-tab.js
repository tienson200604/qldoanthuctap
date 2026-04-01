async function loadMyScores() {
    const studentRegisId = document.getElementById("idStRegisForScore").value;
    if (!studentRegisId) return;

    const response = await getMethod('/api/score_componentApi/student/find-by-studentRegis?studentRegisId=' + studentRegisId);
    if (response) {
        const data = await response.json();
        renderScores(data);
    }
}

function renderScores(data) {
    const scored = data.scored;
    const notScored = data.notScored;
    const body = document.getElementById("listScoreStudent");
    const notScoredList = document.getElementById("listNotScored");
    const containerNotScored = document.getElementById("notScoredContainer");
    const totalScoreBadge = document.getElementById("totalScore");

    let total = 0;
    body.innerHTML = '';
    
    if (scored.length === 0) {
        body.innerHTML = '<tr><td colspan="4" class="text-center text-muted">Chưa có đầu điểm nào được chấm.</td></tr>';
    } else {
        scored.forEach((item, index) => {
            const point = item.point || 0;
            const percent = item.percent || 0;
            total += (point * percent) / 100;
            
            body.innerHTML += `
                <tr>
                    <td class="text-center">${index + 1}</td>
                    <td class="fw-medium">${item.name}</td>
                    <td class="text-center text-primary fw-semibold">${percent}%</td>
                    <td class="text-center">
                        <span class="badge ${point >= 5 ? 'bg-success' : 'bg-danger'} fs-6">${point}</span>
                    </td>
                </tr>
            `;
        });
    }

    // Cập nhật tổng điểm (làm tròn 2 chữ số)
    totalScoreBadge.innerText = `Tổng điểm hiện tại: ${total.toFixed(2)}`;

    // Hiển thị các đầu điểm chưa chấm
    if (notScored && notScored.length > 0) {
        containerNotScored.style.display = 'block';
        notScoredList.innerHTML = '';
        notScored.forEach(item => {
            notScoredList.innerHTML += `<span class="badge bg-secondary">${item.name} (${item.percent}%)</span>`;
        });
    } else {
        containerNotScored.style.display = 'none';
    }
}
