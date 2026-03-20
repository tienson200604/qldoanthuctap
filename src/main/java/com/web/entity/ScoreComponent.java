package com.web.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "score_result")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class ScoreComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    private String name;

    private Float percent;

    private Float point;

    private Long scoreRatioId;

    @ManyToOne
    private StudentRegis studentRegis;
}
