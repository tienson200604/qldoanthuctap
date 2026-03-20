package com.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "work_progress")
@Getter
@Setter
public class WorkProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    private LocalDateTime deadline;

    private String title;

    private Integer onTimeCount = 0;

    private Integer outTimeCount = 0;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    private SemesterTeacher semesterTeacher;

    @Transient
    private Boolean isSubmitted = false;
}
