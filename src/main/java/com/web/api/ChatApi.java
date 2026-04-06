package com.web.api;

import com.web.dto.response.ChatDto;
import com.web.entity.Chatting;
import com.web.entity.User;
import com.web.mapper.UserMapper;
import com.web.repository.ChatRepository;
import com.web.repository.UserRepository;
import com.web.service.UserService;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatApi {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/user/my-chat")
    public ResponseEntity<?> myChat(){
        List<Chatting> result = chatRepository.myChat(userUtils.getUserWithAuthority().getId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/all/getAllUserChat")
    public ResponseEntity<?> getAllUserChat(@RequestParam(value = "search", required = false) String search){
        User currentUser = userUtils.getUserWithAuthority();
        String normalizedSearch = search == null ? "" : search.trim();
        String searchParam = "%" + normalizedSearch + "%";

        Set<User> users = new LinkedHashSet<>(userRepository.getAllUserChat(currentUser.getId(), searchParam));
        if(!normalizedSearch.isBlank()){
            users.addAll(userRepository.searchChatCandidates(currentUser.getId(), searchParam));
        }

        List<ChatDto> chatDtoList = users.stream()
                .map(user -> toChatDto(currentUser.getId(), user))
                .sorted(Comparator.comparing(ChatDto::getTimestamp, Comparator.nullsLast(Timestamp::compareTo)).reversed())
                .collect(Collectors.toList());
        return new ResponseEntity<>(chatDtoList, HttpStatus.OK);
    }

    @GetMapping("/user/getListChat")
    public List<Chatting> getListChat(@RequestParam("idreciver") Long idreciver){
        return chatRepository.findByUser(userUtils.getUserWithAuthority().getId(), idreciver);
    }


    public void sort(ArrayList<ChatDto> sub) {
        Collections.sort(sub, new Comparator<ChatDto>() {
            @Override
            public int compare(ChatDto o1, ChatDto o2) {
                return o2.getTimestamp().compareTo(o1.getTimestamp());
            }
        });
    }

    public String calculateTime(Timestamp t){
        Long now = System.currentTimeMillis();
        Long end = now - t.getTime();
        if(end/1000/60 < 1){
            return "1 min";
        }
        else if(end/1000/60 >= 1 && end/1000/60 < 60){
            Integer x = Math.toIntExact(end / 1000 / 60);
            return x.toString()+" min";
        }
        else if(end/1000/60/60 >= 1 && end/1000/60/60 < 24){
            Integer x = Math.toIntExact(end / 1000 / 60 / 60);
            return x.toString() + " hour";
        }
        else if(end/1000/60/60/24 >= 1){
            Integer x = Math.toIntExact(end / 1000 / 60 / 60 / 24);
            return x.toString() + " day";
        }
        return "0 min";
    }

    private ChatDto toChatDto(Long currentUserId, User user) {
        Chatting lastChatting = chatRepository.findLastChattingBetweenUsers(currentUserId, user.getId());
        if(lastChatting == null){
            return new ChatDto(userMapper.userToUserDto(user), "", "", new Timestamp(0), "");
        }

        String lastContent = Boolean.TRUE.equals(lastChatting.getIsFile())
                ? "Da gui tep dinh kem"
                : Optional.ofNullable(lastChatting.getContent()).orElse("");
        return new ChatDto(
                userMapper.userToUserDto(user),
                lastContent,
                calculateTime(lastChatting.getCreatedDate()),
                lastChatting.getCreatedDate(),
                ""
        );
    }
}
