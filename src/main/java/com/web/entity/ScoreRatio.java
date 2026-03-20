package com.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "score_ratio")
@Getter
@Setter
public class ScoreRatio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Float percent;

    @ManyToOne
    private Semester semester;

}
