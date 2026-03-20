package com.web.dto.response;

import com.web.entity.Semester;
import com.web.entity.SemesterCompany;
import com.web.entity.SemesterTeacher;
import com.web.enums.InternshipType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SemesterTypeResponse {

    private Long id;

    private InternshipType type;

    private Semester semester;

    private List<SemesterTeacher> semesterTeachers = new ArrayList<>();

    private List<SemesterCompany> semesterCompanies = new ArrayList<>();
}
