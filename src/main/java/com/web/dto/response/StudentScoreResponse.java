package com.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentScoreResponse {
    private Long studentRegisId;
    private String studentCode;      // User.code (mã SV)
    private String studentUsername;  // User.username
    private String studentName;      // User.fullname
    private String classname;        // User.classname
    private String semesterYear;     // Semester.yearName
    private String teacherName;      // SemesterTeacher->teacher.fullname
    private String internshipType;   // InternshipType enum
    private Float totalScore;
    private String evaluate;         // nhận xét của GV
    private String rate;             // xếp loại
}
