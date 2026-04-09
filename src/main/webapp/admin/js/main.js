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
        
        // Khởi tạo Global Search sau khi load xong DOM tĩnh
        setTimeout(initGlobalSearch, 100);
});

function initGlobalSearch() {
    const searchBarContainer = document.querySelector('.search-bar');
    const inputElement = document.querySelector('.search-bar input');

    if (!searchBarContainer || !inputElement) return;

    // 1. Chèn DOM Dropdown và chỉnh sửa Input
    inputElement.id = 'global-search-input';
    inputElement.placeholder = 'Tìm kiếm tài khoản, công ty, chức năng...';
    inputElement.setAttribute('autocomplete', 'off');
    searchBarContainer.classList.add('position-relative');
    searchBarContainer.style.width = '350px';

    const dropdownHtml = `
        <div id="global-search-dropdown" class="dropdown-menu w-100 shadow px-0 border-0" style="max-height: 500px; overflow-y: auto; margin-top: 5px; display: none; position: absolute;">
            <div class="p-3 text-center text-muted small" id="global-search-empty">Gõ từ khóa để tìm kiếm...</div>
            <div id="global-search-results"></div>
        </div>
    `;
    searchBarContainer.insertAdjacentHTML('beforeend', dropdownHtml);

    // 2. Chèn CSS Runtime
    const styleId = 'global-search-style';
    if (!document.getElementById(styleId)) {
        const styleHtml = `
            <style id="global-search-style">
                .global-search-group { background: #f8f9fa; padding: 5px 15px; font-weight: bold; font-size: 0.75rem; color: #6c757d; text-transform: uppercase; border-top: 1px solid #eee; border-bottom: 1px solid #eee; }
                .global-search-group:first-child { border-top: none; }
                .dark-mode .global-search-group { background: #212529; color: #adb5bd; border-color: #333; }
                .global-search-item { cursor: pointer; padding: 10px 15px; transition: all 0.2s; display: flex; align-items: center; gap: 10px; text-decoration: none; color: inherit; }
                .global-search-item:hover { background-color: #e9ecef; color: inherit; }
                .dark-mode .global-search-item:hover { background-color: #2c2c2c; color: inherit; text-decoration: none; }
                #global-search-dropdown.show { display: block !important; }
            </style>
        `;
        document.head.insertAdjacentHTML('beforeend', styleHtml);
    }

    // 3. Logic Event
    const searchDropdown = document.getElementById('global-search-dropdown');
    const searchEmpty = document.getElementById('global-search-empty');
    const searchResults = document.getElementById('global-search-results');

    const navigationMenus = [
        { name: "Dashboard (Bảng điều khiển)", url: "index", icon: "bi-speedometer2" },
        { name: "Quản lý tài khoản (Danh sách)", url: "user", icon: "bi-person-badge" },
        { name: "Thêm tài khoản mới", url: "add-user", icon: "bi-person-plus" },
        { name: "Quản lý danh mục", url: "category", icon: "bi-diagram-3" },
        { name: "Thêm danh mục mới", url: "add-category", icon: "bi-plus-circle" },
        { name: "Quản lý bài viết / Tin tức", url: "blog", icon: "bi-calendar-event" },
        { name: "Thêm tin tức / Bài viết mới", url: "add-blog", icon: "bi-plus-circle" },
        { name: "Quản lý công ty (Doanh nghiệp)", url: "company", icon: "bi-building" },
        { name: "Thêm công ty / doanh nghiệp mới", url: "add-company", icon: "bi-plus-circle" },
        { name: "Quản lý tài liệu biểu mẫu", url: "document", icon: "bi-file-earmark-text" },
        { name: "Thêm tài liệu mới", url: "add-document", icon: "bi-plus-circle" },
        { name: "Danh sách các đợt thực tập", url: "semester", icon: "bi-calendar-range" },
        { name: "Quản lý sinh viên thực tập", url: "intern-students", icon: "bi-mortarboard" },
        { name: "Quản lý điểm sv / Thống kê điểm", url: "student-score", icon: "bi-bar-chart-line" },
        { name: "Thống kê đánh giá", url: "rate", icon: "bi-star-fill" },
        { name: "Quản lý log (Lịch sử hoạt động)", url: "log", icon: "bi-file-earmark-text" },
        { name: "Đổi mật khẩu", url: "doi-mat-khau", icon: "bi-key" },
        { name: "Cập nhật Thông tin cá nhân", url: "infor", icon: "bi-person-circle" }
    ];

    let typingTimer;
    const doneTypingInterval = 300;

    inputElement.addEventListener('focus', () => {
        searchDropdown.classList.add('show');
    });

    document.addEventListener('click', (e) => {
        if (!inputElement.contains(e.target) && !searchDropdown.contains(e.target)) {
            searchDropdown.classList.remove('show');
        }
    });

    inputElement.addEventListener('keyup', () => {
        clearTimeout(typingTimer);
        const keyword = inputElement.value.trim().toLowerCase();
        
        if (keyword.length === 0) {
            searchEmpty.style.display = 'block';
            searchResults.innerHTML = '';
            searchEmpty.innerHTML = 'Gõ từ khóa để tìm kiếm...';
            return;
        }

        searchEmpty.style.display = 'block';
        searchEmpty.innerHTML = '<div class="spinner-border spinner-border-sm text-primary" role="status" style="width: 1rem; height: 1rem;"></div> <span class="ms-2">Đang tìm...</span>';
        searchResults.innerHTML = '';
        
        typingTimer = setTimeout(async () => {
            await performSearch(keyword);
        }, doneTypingInterval);
    });

    function removeAccents(str) {
        return str.normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLowerCase();
    }

    async function performSearch(keyword) {
        const unaccentedKeyword = removeAccents(keyword);
        const matchedMenus = navigationMenus.filter(m => {
            return removeAccents(m.name).includes(unaccentedKeyword);
        }).slice(0, 4);
        
        try {
            const tokenToUse = localStorage.getItem('token');
            const [usersRes, companiesRes] = await Promise.all([
                fetch(`/api/user/admin/search?keyword=${encodeURIComponent(keyword)}&size=5`, {
                    headers: { 'Authorization': 'Bearer ' + tokenToUse }
                }),
                fetch(`/api/company/public/find-all?search=${encodeURIComponent(keyword)}&size=3`)
            ]);

            const usersData = await usersRes.json();
            const companiesData = await companiesRes.json();

            renderResults(matchedMenus, usersData.content || [], companiesData.content || []);
        } catch (error) {
            console.error("Lỗi khi tìm kiếm:", error);
            searchEmpty.style.display = 'block';
            searchEmpty.innerHTML = '<span class="text-danger">Có lỗi kết nối API khi tìm kiếm.</span>';
        }
    }

    function renderResults(menus, users, companies) {
        searchEmpty.style.display = 'none';
        searchResults.innerHTML = '';

        if (menus.length === 0 && users.length === 0 && companies.length === 0) {
            searchEmpty.style.display = 'block';
            searchEmpty.innerHTML = 'Không tìm thấy thông tin nào phù hợp.';
            return;
        }

        let html = '';

        if (menus.length > 0) {
            html += `<div class="global-search-group">Tính năng & Trang</div>`;
            menus.forEach(m => {
                html += `
                    <a href="${m.url}" class="global-search-item">
                        <div class="bg-primary bg-opacity-10 text-primary p-2 rounded d-flex align-items-center justify-content-center" style="width: 32px; height: 32px;"><i class="bi ${m.icon}"></i></div>
                        <div>
                            <div class="fw-bold small text-body">${m.name}</div>
                            <div class="text-muted" style="font-size: 11px;">Chuyển hướng nhanh</div>
                        </div>
                    </a>
                `;
            });
        }

        if (users.length > 0) {
            html += `<div class="global-search-group">Hồ sơ người dùng</div>`;
            users.forEach(u => {
                const avatar = u.avatar || '/image/default-avatar.jpg';
                const codeDesc = u.code ? ` - ${u.code}` : '';
                const emailStr = u.email || 'Thêm email: Cập nhật sau';
                let roleDesc = "Người dùng";
                if(u.authorities && u.authorities.length > 0) {
                     roleDesc = u.authorities[0].name === "ROLE_ADMIN" ? "Admin" : (u.authorities[0].name === "ROLE_TEACHER" ? "Giảng viên" : "Sinh viên");
                }
                const badgeColor = roleDesc === "Admin" ? "bg-danger" : (roleDesc === "Giảng viên" ? "bg-primary" : "bg-info text-dark");

                html += `
                    <a href="user" class="global-search-item">
                        <img src="${avatar}" width="36" height="36" class="rounded-circle shadow-sm" style="object-fit: cover;">
                        <div style="flex: 1; min-width: 0;">
                            <div class="fw-bold small text-body text-truncate">${u.fullName || u.username}${codeDesc}</div>
                            <div class="text-muted text-truncate" style="font-size: 11px;">
                                <span class="badge ${badgeColor} me-1" style="font-size: 9px;">${roleDesc}</span> ${emailStr}
                            </div>
                        </div>
                    </a>
                `;
            });
        }

        if (companies.length > 0) {
            html += `<div class="global-search-group">Danh sách Công ty</div>`;
            companies.forEach(c => {
                html += `
                    <a href="company" class="global-search-item">
                        <div class="bg-success bg-opacity-10 text-success p-2 rounded d-flex align-items-center justify-content-center" style="width: 36px; height: 36px;"><i class="bi bi-building"></i></div>
                        <div style="flex: 1; min-width: 0;">
                            <div class="fw-bold small text-body text-truncate">${c.name}</div>
                            <div class="text-muted text-truncate" style="font-size: 11px;">${c.address || 'Chưa cập nhật địa chỉ'}</div>
                        </div>
                    </a>
                `;
            });
        }

        searchResults.innerHTML = html;
    }
}

// Global Auto Focus
document.addEventListener("DOMContentLoaded", function() {
    setTimeout(() => {
        const firstInput = document.querySelector('input:not([type="hidden"]):not([type="search"]):not([type="checkbox"]):not([type="radio"]):not([type="file"]):not([readonly]):not([disabled]):not([name="search"]):not(#global-search-input), textarea:not([readonly]):not([disabled]), select:not([readonly]):not([disabled])');
        if (firstInput) firstInput.focus();
    }, 500);

    if (typeof $ !== 'undefined') {
        $(document).on('shown.bs.modal', '.modal', function () {
            $(this).find('input:not([type="hidden"]):not([type="checkbox"]):not([type="radio"]):not([type="file"]):not([readonly]):not([disabled]), textarea:not([readonly]):not([disabled]), select:not([readonly]):not([disabled])').first().focus();
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
