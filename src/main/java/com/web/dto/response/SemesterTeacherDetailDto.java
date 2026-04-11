package com.web.dto.response;

import com.web.enums.InternshipType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SemesterTeacherDetailDto {

    private Long id;

    private String teacherName;

    private String teacherCode;

    private String teacherEmail;

    private String teacherPhone;

    private String semesterName;

    private InternshipType internshipType;

    private LocalDateTime deadlineRegis;

    private String projectName;

    private String descriptionProject;

    private Integer maxStudents;

    private Integer currentStudents;

    private Integer remainingStudents;

    private Integer totalRegistrations;

    private List<SemesterTeacherStudentDto> students;
}
