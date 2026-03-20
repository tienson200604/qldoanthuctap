package com.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "semester_company")
@Getter
@Setter
public class SemesterCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer maxStudent;

    private Integer currentStudent = 0;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    private Semester semester;

    @ManyToOne
    private Company company;
}
