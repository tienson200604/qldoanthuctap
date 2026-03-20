package com.web.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "work_progress_student")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class WorkProcessStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String file;

    private Float percent;

    private LocalDateTime replayDate;

    private String replay;

    @ManyToOne
    private StudentRegis studentRegis;

    @ManyToOne
    private WorkProcess workProcess;
}
