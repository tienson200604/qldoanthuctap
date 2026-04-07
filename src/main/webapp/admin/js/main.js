var exceptionCode = 417;
var token = localStorage.getItem("token");
$( document ).ready(function() {
    var menu = 
    `<div class="sidebar-title p-4 text-white fw-bold fs-4 text-center">ADMIN</div>
    <div class="menu" id="main-menu">
        <a href="index" class="menu-item"><i class="bi bi-speedometer2"></i> <span class="nav-text">Dashboard</span></a>

        <a href="#" class="menu-item" data-bs-toggle="collapse" data-bs-target="#menu1">
            <i class="bi bi-person-badge"></i> <span class="nav-text">Quản lý tài khoản</span> 
            <i class="bi bi-chevron-down ms-auto nav-text"></i>
        </a>
        <div class="collapse" id="menu1">
            <a href="user" class="menu-item ps-5"><i class="bi bi-list-ul"></i> <span class="nav-text">Danh sách</span></a>
            <a href="add-user" class="menu-item ps-5"><i class="bi bi-person-plus"></i> <span class="nav-text">Thêm mới</span></a>
        </div>

        <a href="#" class="menu-item" data-bs-toggle="collapse" data-bs-target="#menu2">
            <i class="bi bi-diagram-3"></i> <span class="nav-text">Quản lý danh mục</span> 
            <i class="bi bi-chevron-down ms-auto nav-text"></i>
        </a>
        <div class="collapse" id="menu2">
            <a href="category" class="menu-item ps-5"><i class="bi bi-building"></i> <span class="nav-text">Danh sách</span></a>
            <a href="add-category" class="menu-item ps-5"><i class="bi bi-plus-circle"></i> <span class="nav-text">Thêm mới</span></a>
        </div>

        <a href="#" class="menu-item" data-bs-toggle="collapse" data-bs-target="#menu3">
            <i class="bi bi-calendar-event"></i> <span class="nav-text">Quản lý tin tức</span> 
            <i class="bi bi-chevron-down ms-auto nav-text"></i>
        </a>
        <div class="collapse" id="menu3">
            <a href="blog" class="menu-item ps-5"><i class="bi bi-building"></i> <span class="nav-text">Danh sách</span></a>
            <a href="add-blog" class="menu-item ps-5"><i class="bi bi-plus-circle"></i> <span class="nav-text">Thêm mới</span></a>
        </div>

        <a href="#" class="menu-item" data-bs-toggle="collapse" data-bs-target="#menu6">
            <i class="bi bi-calendar-event"></i> <span class="nav-text">Quản lý công ty</span> 
            <i class="bi bi-chevron-down ms-auto nav-text"></i>
        </a>
        <div class="collapse" id="menu6">
            <a href="company" class="menu-item ps-5"><i class="bi bi-building"></i> <span class="nav-text">Danh sách</span></a>
            <a href="add-company" class="menu-item ps-5"><i class="bi bi-plus-circle"></i> <span class="nav-text">Thêm mới</span></a>
        </div>

        <a href="#" class="menu-item" data-bs-toggle="collapse" data-bs-target="#menu7">
            <i class="bi bi-calendar-event"></i> <span class="nav-text">Quản lý tài liệu</span> 
            <i class="bi bi-chevron-down ms-auto nav-text"></i>
        </a>
        <div class="collapse" id="menu7">
            <a href="document" class="menu-item ps-5"><i class="bi bi-building"></i> <span class="nav-text">Danh sách</span></a>
            <a href="add-document" class="menu-item ps-5"><i class="bi bi-plus-circle"></i> <span class="nav-text">Thêm mới</span></a>
        </div>

        <a href="#" class="menu-item" data-bs-toggle="collapse" data-bs-target="#menu8">
            <i class="bi bi-calendar-event"></i> <span class="nav-text">Quản lý Thực tập</span>
            <i class="bi bi-chevron-down ms-auto nav-text"></i>
        </a>
        <div class="collapse" id="menu8">
            <a href="semester" class="menu-item ps-5"><i class="bi bi-building"></i> <span class="nav-text">Danh sách đợt thực tập</span></a>
            <a href="company-semester" class="menu-item ps-5"><i class="bi bi-plus-circle"></i> <span class="nav-text">Công ty</span></a>
            <a href="semester-type" class="menu-item ps-5"><i class="bi bi-plus-circle"></i> <span class="nav-text">Loại thực tập</span></a>
            <a href="semester-teacher" class="menu-item ps-5"><i class="bi bi-person-workspace"></i> <span class="nav-text">Giảng viên hướng dẫn</span></a>
            <a href="intern-students" class="menu-item ps-5"><i class="bi bi-mortarboard"></i> <span class="nav-text">Sinh viên thực tập</span></a>
        </div>

        <a href="#" class="menu-item" data-bs-toggle="collapse" data-bs-target="#menu9">
            <i class="bi bi-calendar-event"></i> <span class="nav-text">Quản lý điểm</span> 
            <i class="bi bi-chevron-down ms-auto nav-text"></i>
        </a>
        <div class="collapse" id="menu9">
            <a href="score" class="menu-item ps-5"><i class="bi bi-building"></i> <span class="nav-text">Danh sách đầu điểm</span></a>
            <a href="student-score" class="menu-item ps-5"><i class="bi bi-bar-chart-line"></i> <span class="nav-text">Thống kê điểm SV</span></a>
            <a href="rate" class="menu-item ps-5"><i class="bi bi-star-fill"></i> <span class="nav-text">Thống kê đánh giá</span></a>
        </div>

        <a href="log" class="menu-item"><i class="bi bi-file-earmark-text"></i> <span class="nav-text">Quản lý log</span></a>

        <a href="#" class="menu-item" data-bs-toggle="collapse" data-bs-target="#menu4">
            <i class="bi bi-gear"></i> <span class="nav-text">Cài đặt</span> 
            <i class="bi bi-chevron-down ms-auto nav-text"></i>
        </a>
        <div class="collapse" id="menu4">
            <a href="doi-mat-khau" class="menu-item ps-5"><i class="bi bi-key"></i> <span class="nav-text">Đổi mật khẩu</span></a>
            <a href="infor" class="menu-item ps-5"><i class="bi bi-person-circle"></i> <span class="nav-text">Thông tin cá nhân</span></a>
            <a href="/logout" class="menu-item ps-5"><i class="bi bi-box-arrow-right"></i> <span class="nav-text">Đăng xuất</span></a>
        </div>
    </div>`
    document.getElementById('sidebar').innerHTML = menu;

    var topHeader = 
    `<div class="d-flex align-items-center gap-3">
        <button id="toggle-sidebar" class="btn btn-light border-0">
            <i class="bi bi-list fs-4"></i>
        </button>
        <div class="search-bar">
            <input type="text" class="form-control" placeholder="Tìm kiếm...">
        </div>
    </div>
    <div class="d-flex align-items-center gap-4">
        <div class="d-flex align-items-center gap-3">
            <i class="bi bi-moon-fill" id="darkModeToggle" title="Bật/Tắt giao diện tối" style="cursor:pointer; font-size: 1.2rem;"></i>
            
            <div class="dropdown">
                <i class="bi bi-bell position-relative" id="notificationBell" data-bs-toggle="dropdown" aria-expanded="false" style="cursor:pointer; font-size: 1.2rem;">
                    <span id="notificationBadge" class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger" style="font-size: 0.6rem; display: none;">0</span>
                </i>
                <ul class="dropdown-menu dropdown-menu-end p-0 shadow-sm" aria-labelledby="notificationBell" style="width: 320px; max-height: 450px; overflow-y: auto;">
                    <li class="p-3 border-bottom d-flex justify-content-between align-items-center">
                        <span class="fw-bold">Thông báo mới</span>
                        <a href="#" class="text-primary" style="font-size: 0.75rem; text-decoration: none;" onclick="markAllAsRead(event)">Đánh dấu đã xem</a>
                    </li>
                    <div id="notificationList">
                        <li class="p-3 text-center text-muted small">Không có thông báo nào</li>
                    </div>
                </ul>
            </div>
        </div>
        <div class="user-profile">
            <img src="/image/default-avatar.jpg" id="avatar-header" width="40" class="rounded-circle" >
            <div>
                <div class="fw-bold small" id="fullname-header">Nguyễn Văn A</div>
                <div class="text-muted" style="font-size: 0.7rem;">Quản trị viên</div>
            </div>
        </div>
    </div>
    <style>
        .notification-item { cursor: pointer; transition: background 0.2s; border-bottom: 1px solid #f1f1f1; padding: 12px 15px; }
        .notification-item:hover { background: #f8f9fa; }
        .notification-item.unread { background: #f0f7ff; border-left: 3px solid #0d6efd; }
        .dark-mode .notification-item { border-bottom-color: #333; }
        .dark-mode .notification-item:hover { background: #2c2c2c; }
        .dark-mode .notification-item.unread { background: #1a2a3a; }
    </style>`;
    document.getElementById('top-header').innerHTML = topHeader;
    
    // Khởi tạo thông báo
    fetchNotifications();
    connect();
    const toggleBtn = document.getElementById('toggle-sidebar');
    const sidebar = document.getElementById('sidebar');
    const header = document.getElementById('top-header');
    const content = document.getElementById('main-content');

    toggleBtn.addEventListener('click', () => {
        sidebar.classList.toggle('collapsed');
        // Lưu trạng thái vào trình duyệt để khi load lại trang không bị mất
        const isCollapsed = sidebar.classList.contains('collapsed');
        localStorage.setItem('sidebarState', isCollapsed);
    });

    var user = localStorage.getItem('user');
    user = JSON.parse(user);
    document.getElementById('fullname-header').textContent = user.fullName;
    if(user.avatar != null && user.avatar != '') {
        document.getElementById('avatar-header').src = user.avatar;
    }

    // Giữ trạng thái khi load lại trang
    // window.onload = () => {
    //     if (localStorage.getItem('sidebarState') === 'true') {
    //         sidebar.classList.add('collapsed');
    //     }
    // };

    // Dark mode logic
    const darkModeToggleIcon = document.getElementById("darkModeToggle");
    if (darkModeToggleIcon) {
        const isDarkMode = localStorage.getItem("darkMode") === "true";
        if (isDarkMode) {
            document.body.classList.add("dark-mode");
            document.documentElement.setAttribute('data-bs-theme', 'dark');
            darkModeToggleIcon.classList.replace("bi-moon-fill", "bi-sun-fill");
        }
        darkModeToggleIcon.addEventListener("click", function() {
            document.body.classList.toggle("dark-mode");
            const isDark = document.body.classList.contains("dark-mode");
            localStorage.setItem("darkMode", isDark);
            
            if (isDark) {
                document.documentElement.setAttribute('data-bs-theme', 'dark');
                darkModeToggleIcon.classList.replace("bi-moon-fill", "bi-sun-fill");
            } else {
                document.documentElement.removeAttribute('data-bs-theme');
                darkModeToggleIcon.classList.replace("bi-sun-fill", "bi-moon-fill");
            }
        });
    }
});

function loadInforHeader() {
}

function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    window.location.href = "/login";
}

// Logic thông báo Admin
let stompClient = null;

function connect() {
    if (typeof SockJS === 'undefined' || typeof Stomp === 'undefined') {
        console.warn('Stomp or SockJS not found, loading libraries...');
        loadScript("https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.1/sockjs.min.js", () => {
            loadScript("https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js", () => {
                startConnect();
            });
        });
    } else {
        startConnect();
    }
}

function loadScript(url, callback) {
    let script = document.createElement("script");
    script.type = "text/javascript";
    script.src = url;
    script.onload = callback;
    document.head.appendChild(script);
}

function startConnect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.debug = null; // Tắt log debug
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/user/queue/notification', function (message) {
            const data = JSON.parse(message.body);
            toastr.info(data.content, data.title);
            fetchNotifications();
        });
    }, function (error) {
        setTimeout(connect, 5000);
    });
}

async function fetchNotifications() {
    try {
        const [countRes, topRes] = await Promise.all([
            fetch('/api/notification/all/count-noti'),
            fetch('/api/notification/all/top-noti')
        ]);
        const count = await countRes.json();
        const topNotis = await topRes.json();
        updateBadge(count);
        renderNotifications(topNotis);
    } catch (e) {
        console.error('Failed to fetch notifications', e);
    }
}

function updateBadge(count) {
    const badge = document.getElementById('notificationBadge');
    if (!badge) return;
    if (count > 0) {
        badge.innerText = count > 99 ? '99+' : count;
        badge.style.display = 'block';
    } else {
        badge.style.display = 'none';
    }
}

function renderNotifications(notis) {
    const list = document.getElementById('notificationList');
    if (!list) return;
    if (!notis || notis.length === 0) {
        list.innerHTML = '<li class="p-3 text-center text-muted small">Không có thông báo nào</li>';
        return;
    }
    list.innerHTML = notis.map(n => `
        <li class="p-3 notification-item ${n.isRead ? '' : 'unread'}" onclick="window.location.href='${n.link || '#'}'">
            <div class="fw-bold small">${n.title}</div>
            <div class="text-muted" style="font-size: 0.75rem;">${n.content || n.title}</div>
            <div class="text-end" style="font-size: 0.65rem; color: #999; margin-top: 5px;">
                ${new Date(n.createdDate).toLocaleTimeString('vi-VN', {hour: '2-digit', minute:'2-digit'})} 
                ${new Date(n.createdDate).toLocaleDateString('vi-VN')}
            </div>
        </li>
    `).join('');
}

async function markAllAsRead(e) {
    if(e) {
        e.preventDefault();
        e.stopPropagation();
    }
    try {
        await fetch('/api/notification/all/mark-read', { method: 'POST' });
        fetchNotifications();
    } catch (e) {
        console.error('Failed to mark as read', e);
    }
}
