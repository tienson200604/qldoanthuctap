let privateChatStompClient = null;
let privateChatSelectedUser = null;
let privateChatContacts = [];
let privateChatSearchTimer = null;

document.addEventListener("DOMContentLoaded", function () {
    connectPrivateChat();
    loadPrivateChatContacts();

    const params = new URLSearchParams(window.location.search);
    const presetUserId = params.get("userId");
    if (presetUserId) {
        loadPrivateChatContacts("", Number(presetUserId));
    }

    const input = document.getElementById("chatMessageInput");
    input.addEventListener("keydown", function (event) {
        if (event.key === "Enter" && !event.shiftKey) {
            event.preventDefault();
            sendPrivateMessage();
        }
    });
});

function connectPrivateChat() {
    const currentUsername = document.getElementById("currentUsername").value;
    const socket = new SockJS("/hello");
    privateChatStompClient = Stomp.over(socket);
    privateChatStompClient.connect({username: currentUsername}, function () {
        privateChatStompClient.subscribe("/users/queue/messages", function () {
            loadPrivateChatContacts(document.getElementById("chatSearch").value.trim(), privateChatSelectedUser ? privateChatSelectedUser.id : null);
            if (privateChatSelectedUser) {
                loadPrivateChatMessages(privateChatSelectedUser.id);
            }
        });
    }, function () {
        setTimeout(connectPrivateChat, 3000);
    });
}

function handleChatSearch(event) {
    if (privateChatSearchTimer) {
        clearTimeout(privateChatSearchTimer);
    }
    privateChatSearchTimer = setTimeout(function () {
        loadPrivateChatContacts(event.target.value.trim());
    }, 250);
}

async function loadPrivateChatContacts(search = "", preferredUserId = null) {
    const response = await getMethod("/api/chat/all/getAllUserChat?search=" + encodeURIComponent(search));
    if (!response) {
        return;
    }
    privateChatContacts = await response.json();
    renderPrivateChatContacts();

    let userToSelect = null;
    if (preferredUserId) {
        userToSelect = privateChatContacts.find(item => item.user.id === preferredUserId);
    }
    if (!userToSelect && privateChatSelectedUser) {
        userToSelect = privateChatContacts.find(item => item.user.id === privateChatSelectedUser.id);
    }
    if (!userToSelect && privateChatContacts.length > 0) {
        userToSelect = privateChatContacts[0];
    }
    if (userToSelect) {
        selectPrivateChatUser(userToSelect.user.id);
    } else {
        showPrivateChatEmptyState();
    }
}

function renderPrivateChatContacts() {
    const contactList = document.getElementById("contactList");
    if (!privateChatContacts || privateChatContacts.length === 0) {
        contactList.innerHTML = '<div class="private-chat-contact-empty">Chưa có hội thoại nào. Hãy tìm người dùng để bắt đầu chat.</div>';
        return;
    }

    contactList.innerHTML = privateChatContacts.map(item => {
        const user = item.user;
        const avatar = user.avatar && user.avatar !== "" ? user.avatar : "/image/default-avatar.jpg";
        const role = user.authorities && user.authorities.name ? mapPrivateChatRole(user.authorities.name) : "";
        const activeClass = privateChatSelectedUser && privateChatSelectedUser.id === user.id ? "active" : "";
        return `
            <div class="private-chat-contact ${activeClass}" onclick="selectPrivateChatUser(${user.id})">
                <img src="${avatar}" class="private-chat-contact-avatar">
                <div class="flex-grow-1 min-w-0">
                    <div class="d-flex justify-content-between gap-2">
                        <div class="private-chat-contact-title">${escapePrivateChatHtml(user.fullname || user.email || user.username)}</div>
                        <div class="private-chat-contact-meta">${escapePrivateChatHtml(item.time || "")}</div>
                    </div>
                    <div class="private-chat-contact-meta">${escapePrivateChatHtml(role)}${role && user.code ? " - " : ""}${escapePrivateChatHtml(user.code || "")}</div>
                    <div class="private-chat-contact-last">${escapePrivateChatHtml(item.lastContent || "Chưa có tin nhắn")}</div>
                </div>
            </div>
        `;
    }).join("");
}

async function selectPrivateChatUser(userId) {
    const selected = privateChatContacts.find(item => item.user.id === userId);
    if (!selected) {
        return;
    }
    privateChatSelectedUser = selected.user;
    renderPrivateChatContacts();
    showPrivateChatContent(selected.user);
    await loadPrivateChatMessages(userId);
}

function showPrivateChatContent(user) {
    document.getElementById("chatEmptyState").classList.add("d-none");
    document.getElementById("chatContent").classList.remove("d-none");
    document.getElementById("selectedUserAvatar").src = user.avatar && user.avatar !== "" ? user.avatar : "/image/default-avatar.jpg";
    document.getElementById("selectedUserName").textContent = user.fullname || user.email || user.username;
    const metaParts = [];
    if (user.authorities && user.authorities.name) {
        metaParts.push(mapPrivateChatRole(user.authorities.name));
    }
    if (user.code) {
        metaParts.push(user.code);
    }
    if (user.email) {
        metaParts.push(user.email);
    }
    document.getElementById("selectedUserMeta").textContent = metaParts.join(" - ");
}

function showPrivateChatEmptyState() {
    privateChatSelectedUser = null;
    document.getElementById("chatEmptyState").classList.remove("d-none");
    document.getElementById("chatContent").classList.add("d-none");
    document.getElementById("chatMessages").innerHTML = "";
}

async function loadPrivateChatMessages(userId) {
    const response = await getMethod("/api/chat/user/getListChat?idreciver=" + userId);
    if (!response) {
        return;
    }
    const messages = await response.json();
    const container = document.getElementById("chatMessages");
    const currentUserId = Number(document.getElementById("currentUserId").value);

    if (!messages || messages.length === 0) {
        container.innerHTML = '<div class="text-center text-muted mt-5">Chưa có tin nhắn nào. Hãy bắt đầu cuộc trò chuyện.</div>';
        return;
    }

    container.innerHTML = messages.map(item => renderPrivateChatMessage(item, currentUserId)).join("");
    container.scrollTop = container.scrollHeight;
}

function renderPrivateChatMessage(message, currentUserId) {
    const isMine = message.sender && message.sender.id === currentUserId;
    const avatar = message.sender && message.sender.avatar ? message.sender.avatar : "/image/default-avatar.jpg";
    const senderName = isMine ? "Bạn" : ((message.sender && message.sender.fullname) ? message.sender.fullname : "Người dùng");
    return `
        <div class="private-chat-message ${isMine ? "mine" : ""}">
            <img src="${avatar}" class="private-chat-contact-avatar">
            <div class="private-chat-bubble">
                <div class="private-chat-message-meta">
                    <span>${escapePrivateChatHtml(senderName)}</span>
                    <span>${escapePrivateChatHtml(formatPrivateChatDate(message.createdDate))}</span>
                </div>
                <div class="private-chat-message-content">${renderPrivateChatContent(message)}</div>
            </div>
        </div>
    `;
}

function renderPrivateChatContent(message) {
    if (message.isFile) {
        const fileName = message.fileName || "Tệp đính kèm";
        if (isPrivateChatImage(fileName, message.content)) {
            return `
                <a href="${message.content}" target="_blank" rel="noopener noreferrer">
                    <img src="${message.content}" alt="${escapePrivateChatHtml(fileName)}">
                </a>
                <a href="${message.content}" target="_blank" rel="noopener noreferrer" class="private-chat-file-link text-reset text-decoration-underline mt-2">
                    <i class="bi bi-image"></i> ${escapePrivateChatHtml(fileName)}
                </a>
            `;
        }
        return `<a href="${message.content}" target="_blank" rel="noopener noreferrer" class="private-chat-file-link text-reset text-decoration-underline"><i class="bi bi-paperclip"></i> ${escapePrivateChatHtml(fileName)}</a>`;
    }
    return escapePrivateChatHtml(message.content || "").replace(/\n/g, "<br>");
}

function sendPrivateMessage() {
    if (!privateChatSelectedUser) {
        swal("Thông báo", "Vui lòng chọn người nhận trước khi gửi tin nhắn", "warning");
        return;
    }
    const input = document.getElementById("chatMessageInput");
    const message = input.value.trim();
    if (message === "") {
        swal("Thông báo", "Vui lòng nhập nội dung tin nhắn", "warning");
        return;
    }
    if (!privateChatStompClient || !privateChatStompClient.connected) {
        swal("Thông báo", "Kết nối chat chưa sẵn sàng, vui lòng thử lại", "warning");
        return;
    }

    privateChatStompClient.send("/app/hello/" + privateChatSelectedUser.id, {}, message);
    input.value = "";
    setTimeout(function () {
        loadPrivateChatContacts(document.getElementById("chatSearch").value.trim(), privateChatSelectedUser.id);
        loadPrivateChatMessages(privateChatSelectedUser.id);
    }, 200);
}

async function sendPrivateFile(input) {
    if (!privateChatSelectedUser) {
        input.value = "";
        swal("Thông báo", "Vui lòng chọn người nhận trước khi gửi tệp", "warning");
        return;
    }
    if (!input.files || input.files.length === 0) {
        return;
    }
    if (!privateChatStompClient || !privateChatStompClient.connected) {
        input.value = "";
        swal("Thông báo", "Kết nối chat chưa sẵn sàng, vui lòng thử lại", "warning");
        return;
    }

    const file = input.files[0];
    if (file.size > 10 * 1024 * 1024) {
        input.value = "";
        swal("Thông báo", "Tệp tải lên không được vượt quá 10MB", "warning");
        return;
    }

    const statusElement = document.getElementById("chatUploadStatus");
    statusElement.textContent = "Đang tải tệp lên...";

    try {
        const formData = new FormData();
        formData.append("file", file);
        const fileUrl = await uploadSingleFileFormData(formData);
        if (!fileUrl) {
            statusElement.textContent = "";
            input.value = "";
            return;
        }
        privateChatStompClient.send("/app/file/" + privateChatSelectedUser.id + "/" + encodeURIComponent(file.name), {}, fileUrl);
        statusElement.textContent = "Đã gửi: " + file.name;
        setTimeout(function () {
            statusElement.textContent = "";
        }, 3000);
        setTimeout(function () {
            loadPrivateChatContacts(document.getElementById("chatSearch").value.trim(), privateChatSelectedUser.id);
            loadPrivateChatMessages(privateChatSelectedUser.id);
        }, 200);
    } finally {
        input.value = "";
    }
}

function mapPrivateChatRole(role) {
    if (role === "ROLE_TEACHER") return "Giảng viên";
    if (role === "ROLE_STUDENT") return "Sinh viên";
    if (role === "ROLE_ADMIN") return "Quản trị viên";
    return role;
}

function formatPrivateChatDate(value) {
    if (!value) {
        return "";
    }
    return new Date(value).toLocaleString("vi-VN", {
        hour: "2-digit",
        minute: "2-digit",
        day: "2-digit",
        month: "2-digit",
        year: "numeric"
    });
}

function escapePrivateChatHtml(value) {
    return String(value || "")
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#39;");
}

function isPrivateChatImage(fileName, url) {
    const target = (fileName || url || "").toLowerCase();
    return /\.(png|jpg|jpeg|gif|webp|bmp|svg)$/.test(target);
}
