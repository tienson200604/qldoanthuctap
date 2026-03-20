package com.web.dto.request;

import com.web.enums.InternshipType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentRegisRequest {

    private String companyName;

    private String companyAddress;

    private String companyPhone;

    private String taxCode;

    private String introductionPaper;

    private InternshipType internshipType;

    private Long semesterTeacherId;

    private Long semesterCompanyId;
}
