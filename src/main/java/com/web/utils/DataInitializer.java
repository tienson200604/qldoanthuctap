package com.web.utils;

import com.web.entity.Authority;
import com.web.entity.User;
import com.web.enums.UserType;
import com.web.repository.AuthorityRepository;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Khởi tạo các Quyền (Roles) nếu chưa có
        String[] roles = { Contains.ROLE_ADMIN, Contains.ROLE_TEACHER, Contains.ROLE_STUDENT, Contains.ROLE_PARENT };
        for (String roleName : roles) {
            if (!authorityRepository.findById(roleName).isPresent()) {
                Authority authority = new Authority();
                authority.setName(roleName);
                authorityRepository.save(authority);
            }
        }

        String username = "admin@gmail.com";
        String password = "admin";
        String email = "admin@gmail.com";

        // 2. Kiểm tra và tạo tài khoản Admin
        if (!userRepository.findByUsername(username).isPresent()) {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setUserType(UserType.EMAIL);
            user.setActived(true);
            user.setCreatedDate(new Date(System.currentTimeMillis()));
            user.setFullname("ADMIN");
            user.setAuthorities(authorityRepository.findById(Contains.ROLE_ADMIN).get());
            userRepository.save(user);
        }
    }
}
