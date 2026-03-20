package com.web.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.enums.InternshipType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "semester_type")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class SemesterType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    private LocalDateTime updateDate;

    private LocalDateTime deadlineRegis;

    @Enumerated(EnumType.STRING)
    private InternshipType type;

    @ManyToOne
    private Semester semester;

}
