package com.web.dto.response;

import com.web.enums.InternshipType;
import com.web.enums.StudentRegisStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StudentRegisAdminDto {

    private Long id;
    private String studentName;
    private String studentCode;
    private String className;
    private String semesterName;
    private InternshipType internshipType;
    private String teacherName;
    private String companyName;
    private StudentRegisStatus status;
    private LocalDateTime registerDate;
}
