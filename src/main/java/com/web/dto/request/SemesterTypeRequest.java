package com.web.dto.request;

import com.web.enums.InternshipType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SemesterTypeRequest {

    private Long id;

    private InternshipType type;

    private Long semesterId;

    private LocalDateTime deadlineRegis;

    private List<SemesterTeacherRequest> semesterTeacherRequests = new ArrayList<>();

    @Getter
    @Setter
    public static class SemesterTeacherRequest {

        private Long id;

        private Integer maxStudents;

        private Long teacherId;

        private String projectName;

        private String descriptionProject;

    }

}