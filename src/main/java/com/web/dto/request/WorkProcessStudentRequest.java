package com.web.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkProcessStudentRequest {

    private Long id;

    private String title;

    private String content;

    private String file;

    private Float percent;

    private Long workProcessId;
}