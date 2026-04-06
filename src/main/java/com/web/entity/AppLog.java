package com.web.entity;

import com.web.enums.LogLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "app_log")
@Getter
@Setter
public class AppLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @Column(columnDefinition = "TEXT")
    private String actionContent;

    @Enumerated(EnumType.STRING)
    private LogLevel logLevel;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
