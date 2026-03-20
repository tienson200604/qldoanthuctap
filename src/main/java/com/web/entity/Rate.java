package com.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rate")
@Getter
@Setter
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer q1 = 3; // phản hồi
    private Integer q2 = 3; // hỗ trợ
    private Integer q3 = 3; // chuyên môn

    private Double avgScore;

    private String comment;

    @ManyToOne
    private StudentRegis studentRegis;
}
