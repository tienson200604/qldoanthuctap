package com.web.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WorkProcessRequest {

    private Long id;

    private LocalDateTime deadline;

    private String title;

    private String description;

    private Long semesterTeacherId;
}