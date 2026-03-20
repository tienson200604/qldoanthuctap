package com.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class QuanlydoanApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuanlydoanApplication.class, args);
    }

}
