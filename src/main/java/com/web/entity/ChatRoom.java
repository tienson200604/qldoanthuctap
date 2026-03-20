package com.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "chat_room")
@Getter
@Setter
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Timestamp createdDate;

    @ManyToOne
    @JoinColumn(name = "sender")
    private User sender;

    private Long semesterTeacherId;
}
