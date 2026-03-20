package com.web.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "semester_teacher")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class SemesterTeacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    @JsonFormat(pattern = "hh:mm dd/MM/YYYY")
    private LocalDateTime createdDate;

    private Integer maxStudents;

    private Integer currentStudents = 0;

    private String projectName;

    private String descriptionProject;

    @ManyToOne
    private User teacher;

    @ManyToOne
    private SemesterType semesterType;
}
