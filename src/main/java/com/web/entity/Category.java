package com.web.entity;

import com.web.enums.CategoryType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "category")
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String image;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

}
