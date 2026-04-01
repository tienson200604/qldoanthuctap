package com.web.service;

import com.web.dto.ai.GeminiRequest;
import com.web.dto.ai.GeminiResponse;
import com.web.entity.Blog;
import com.web.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AIChatBotService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=";

    @Autowired
    private BlogRepository blogRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public String chat(String userMessage) {
        String msgLower = userMessage.toLowerCase().trim();
        
        // 1. Phản hồi nhanh cho các câu chào hỏi đơn giản để tiết kiệm Quota
        if (msgLower.equals("hi") || msgLower.equals("chào") || msgLower.equals("hello") || msgLower.equals("chao")) {
            return "Xin chào! Tôi là Trợ lý AI. Tôi có thể giúp gì cho bạn về quy trình thực tập hôm nay?";
        }

        // 2. Chỉ lấy tối đa 5 Blog mới nhất để làm ngữ cảnh
        List<Blog> blogs = blogRepository.newBlog();
        if (blogs.size() > 5) {
            blogs = blogs.subList(0, 5);
        }

        String context = blogs.stream()
                .map(b -> "- " + b.getTitle() + ": " + b.getDescription())
                .collect(Collectors.joining("\n"));

        // 3. Xây dựng System Prompt tối ưu cho Gemini 2.5
        String systemPrompt = "Bạn là trợ lý ảo của Hệ thống Quản lý Đồ án. Hãy dùng thông tin sau để trả lời:\n" +
                context + "\n\n" +
                "Câu hỏi sinh viên: " + userMessage + "\n" +
                "Yêu cầu: Trả lời ngắn gọn, tiếng Việt lịch sự.";

        // 4. Chuẩn bị Request
        GeminiRequest request = new GeminiRequest();
        List<GeminiRequest.Content> contents = new ArrayList<>();
        List<GeminiRequest.Part> parts = new ArrayList<>();
        parts.add(new GeminiRequest.Part(systemPrompt));
        contents.add(new GeminiRequest.Content(parts));
        request.setContents(contents);

        // 5. Gọi API với xử lý lỗi 429 cụ thể
        try {
            GeminiResponse response = restTemplate.postForObject(GEMINI_API_URL + apiKey, request, GeminiResponse.class);
            return response != null ? response.getText() : "AI đang bận xử lý, bạn thử lại sau ít giây nhé.";
        } catch (org.springframework.web.client.HttpClientErrorException.TooManyRequests e) {
            return "Hệ thống AI (Free Tier) đang đạt giới hạn. Bạn vui lòng đợi 10 giây nhé!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi AI: " + e.getMessage();
        }
    }
}
