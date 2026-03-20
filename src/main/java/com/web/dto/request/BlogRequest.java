package com.web.dto.request;

import com.web.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class BlogRequest {

    private Long id;

    private String title;

    private String description;

    private String content;

    private String image;

    private Long categoryId;

}
