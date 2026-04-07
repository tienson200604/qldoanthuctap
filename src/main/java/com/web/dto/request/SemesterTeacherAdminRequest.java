package com.web.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SemesterTeacherAdminRequest {

    private Long id;

    private Long semesterTypeId;

    private Long teacherId;

    private Integer maxStudents;

    private String projectName;

    private String descriptionProject;
}
