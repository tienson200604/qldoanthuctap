package com.web.api;

import com.web.service.AIChatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin
public class AIApi {

    @Autowired
    private AIChatBotService aiChatBotService;

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        if (message == null || message.isEmpty()) {
            return ResponseEntity.badRequest().body("Tin nhắn không được để trống.");
        }
        String response = aiChatBotService.chat(message);
        return ResponseEntity.ok(Map.of("response", response));
    }
}
