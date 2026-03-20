package com.web.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "related_documents")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class RelatedDocuments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Integer onTimeCount = 0;

    private Integer outTimeCount = 0;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime deadline;

    @ManyToOne
    private SemesterTeacher semesterTeacher;


}
