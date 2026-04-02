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
import java.util.Arrays;
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
        
        // 1. Phản hồi nhanh cho các câu chào hỏi đơn giản
        if (msgLower.equals("hi") || msgLower.equals("chào") || msgLower.equals("hello") || msgLower.equals("chao")) {
            return "Xin chào! Tôi là Trợ lý AI. Tôi có thể giúp gì cho bạn về quy trình thực tập hôm nay?";
        }

        // 2. Tìm kiếm thông minh: Tìm các Blog chứa từ khóa trong câu hỏi
        List<String> keywords = Arrays.asList(msgLower.split("\\s+"));
        List<Blog> relatedBlogs = new ArrayList<>();
        
        // Chỉ lấy các từ khóa có nghĩa (độ dài > 3 ký tự) để tìm kiếm
        for (String word : keywords) {
            if (word.length() > 3) {
                relatedBlogs.addAll(blogRepository.search(word));
            }
        }
        
        // Loại bỏ trùng lặp và lấy tối đa 5 bài liên quan nhất
        List<Blog> finalBlogs = relatedBlogs.stream()
                .distinct()
                .limit(5)
                .collect(Collectors.toList());

        // Nếu không tìm thấy bài liên quan, mới lấy 5 bài mới nhất làm dự phòng
        if (finalBlogs.isEmpty()) {
            finalBlogs = blogRepository.newBlog();
            if (finalBlogs.size() > 5) {
                finalBlogs = finalBlogs.subList(0, 5);
            }
        }

        String context = finalBlogs.stream()
                .map(b -> "- " + b.getTitle() + ": " + b.getDescription())
                .collect(Collectors.joining("\n"));

        // 3. Xây dựng System Prompt tối ưu
        String systemPrompt = "Bạn là trợ lý ảo của Hệ thống Quản lý Đồ án. Hãy dùng thông tin sau để trả lời:\n" +
                context + "\n\n" +
                "Câu hỏi sinh viên: " + userMessage + "\n" +
                "Yêu cầu: Trả lời ngắn gọn, tiếng Việt lịch sự. Nếu không có thông tin trong dữ liệu, hãy tư vấn chung.";

        // 4. Chuẩn bị Request
        GeminiRequest request = new GeminiRequest();
        List<GeminiRequest.Content> contents = new ArrayList<>();
        List<GeminiRequest.Part> parts = new ArrayList<>();
        parts.add(new GeminiRequest.Part(systemPrompt));
        contents.add(new GeminiRequest.Content(parts));
        request.setContents(contents);

        // 5. Gọi API
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
