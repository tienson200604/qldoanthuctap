package com.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "semester")
@Getter
@Setter
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String yearName;

    private Date startDate;

    private Date endDate;

    private Boolean isActive = false;

}
