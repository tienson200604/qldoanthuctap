package com.web.utils;

import com.web.config.SecurityUtils;
import com.web.dto.response.CustomUserDetails;
import com.web.entity.User;
import com.web.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
//@Transactional
public class UserUtils implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserUtils.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.get() == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user.get());
    }

    public User getUserWithAuthority() {
        try {
            // 1. Lấy thông tin từ SecurityContextHolder thông qua SecurityUtils của bạn
            return SecurityUtils.getCurrentUserLogin()
                    .flatMap(login -> {
                        try {
                            // Chuyển đổi ID từ String sang Long
                            Long id = Long.valueOf(login);
                            return userRepository.findById(id);
                        } catch (NumberFormatException e) {
                            log.error("ID người dùng trong JWT không hợp lệ: {}", login);
                            return Optional.empty();
                        }
                    })
                    .orElse(null); // Trả về null nếu không tìm thấy hoặc lỗi
        } catch (Exception e) {
            System.out.println();
            log.error("Lỗi khi lấy thông tin người dùng hiện tại:", e);
            return null;
        }
    }

    public String randomKey(){
        String str = "12345667890";
        Integer length = str.length()-1;
        StringBuilder stringBuilder = new StringBuilder("");
        for(int i=0; i<6; i++){
            Integer ran = (int)(Math.random()*length);
            stringBuilder.append(str.charAt(ran));
        }
        return String.valueOf(stringBuilder);
    }

    public String randomPass(){
        String str = "qwert1yui2op3as4dfg5hj6klzx7cvb8nmQ9WE0RTYUIOPASDFGHJKLZXCVBNM";
        Integer length = str.length()-1;
        StringBuilder stringBuilder = new StringBuilder("");
        for(int i=0; i<7; i++){
            Integer ran = (int)(Math.random()*length);
            stringBuilder.append(str.charAt(ran));
        }
        return String.valueOf(stringBuilder);
    }
}

