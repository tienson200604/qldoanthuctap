package com.web.api;

import com.web.entity.ChatRoom;
import com.web.entity.Chatting;
import com.web.repository.ChatRepository;
import com.web.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatRoomApi {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @GetMapping("/room/{id}")
    public List<ChatRoom> loadMessage(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page
    ) {
        Pageable pageable = PageRequest.of(page, 4);

        Page<ChatRoom> result = chatRoomRepository.findByRoom(id, pageable);
        return result.getContent();
    }
}
