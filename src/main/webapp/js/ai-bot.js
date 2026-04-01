$(document).ready(function() {
    const chatBtn = $('#ai-chat-button');
    const chatWindow = $('#ai-chat-window');
    const closeBtn = $('#ai-chat-header .close-btn');
    const sendBtn = $('#ai-send-btn');
    const chatInput = $('#ai-chat-input');
    const chatMessages = $('#ai-chat-messages');
    const typingIndicator = $('.typing-indicator');

    // Toggle Chat Window
    chatBtn.on('click', function() {
        chatWindow.toggle();
        if (chatWindow.is(':visible')) {
            if (chatMessages.children().length === 0) {
                addBotMessage("Xin chào! Tôi là Trợ lý AI của Hệ thống Quản lý Đồ án. Tôi có thể giúp gì cho bạn?");
            }
            scrollToBottom();
            chatInput.focus();
        }
    });

    closeBtn.on('click', function() {
        chatWindow.hide();
    });

    function scrollToBottom() {
        setTimeout(function() {
            chatMessages.animate({
                scrollTop: chatMessages[0].scrollHeight
            }, 300);
        }, 50);
    }

    // Send message logic
    function sendMessage() {
        const message = chatInput.val().trim();
        if (message === "") return;

        addUserMessage(message);
        chatInput.val("");
        
        typingIndicator.show();
        scrollToBottom();

        $.ajax({
            url: "/api/ai/chat",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({ message: message }),
            success: function(response) {
                typingIndicator.hide();
                addBotMessage(response.response);
                scrollToBottom();
                chatInput.focus();
            },
            error: function(xhr, status, error) {
                typingIndicator.hide();
                addBotMessage("Hệ thống đang gặp sự cố nhỏ. Vui lòng thử lại sau vài giây.");
                console.error("AI Error:", error);
                scrollToBottom();
            }
        });
    }

    sendBtn.on('click', sendMessage);

    chatInput.on('keypress', function(e) {
        if (e.which === 13) {
            sendMessage();
        }
    });

    function addUserMessage(text) {
        const msgHtml = `<div class="ai-message user">${text}</div>`;
        chatMessages.append(msgHtml);
    }

    function addBotMessage(text) {
        const formattedText = text.replace(/\n/g, '<br/>');
        const msgHtml = `<div class="ai-message bot">${formattedText}</div>`;
        chatMessages.append(msgHtml);
    }
});
