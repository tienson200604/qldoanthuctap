package com.web.api;
import com.web.entity.ChatRoom;
import com.web.entity.Chatting;
import com.web.entity.User;
import com.web.repository.ChatRepository;
import com.web.repository.ChatRoomRepository;
import com.web.repository.UserRepository;
import com.web.utils.Contains;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.sql.Timestamp;
import java.util.*;

@Controller
public class MessageApi {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @MessageMapping("/chat-room/{id}")
    public void sendRoom(SimpMessageHeaderAccessor sha, @Payload String message, @DestinationVariable String id) {
        System.out.println("sha: "+sha.getUser().getName());
        System.out.println("payload: "+message);
        User sender = userRepository.findById(Long.valueOf(sha.getUser().getName())).get();
        ChatRoom chatting = new ChatRoom();
        chatting.setContent(message);
        chatting.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        chatting.setSemesterTeacherId(Long.valueOf(id));
        chatting.setSender(sender);
        chatRoomRepository.save(chatting);
        simpMessagingTemplate.convertAndSend("/topic/chat-room/"+id, chatting);
    }


}
