package com.web.dto.response;

import com.web.enums.InternshipType;
import com.web.enums.StudentRegisStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SemesterTeacherStudentDto {

    private Long studentRegisId;

    private Long studentId;

    private String studentName;

    private String studentCode;

    private String className;

    private String email;

    private String phone;

    private InternshipType internshipType;

    private String companyName;

    private StudentRegisStatus status;

    private Float totalScore;

    private LocalDateTime registerDate;
}
