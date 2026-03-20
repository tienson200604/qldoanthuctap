package com.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.entity.SemesterTeacher;
import com.web.entity.StudentRegis;
import com.web.entity.WorkProcess;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.time.LocalDateTime;

@Getter
@Setter
public class WorkProcessResponse {

    private Long id;

    private LocalDateTime createdDate;

    private LocalDateTime deadline;

    private String title;

    private Integer onTimeCount = 0;

    private Integer outTimeCount = 0;

    private String description;

    private SemesterTeacher semesterTeacher;

    private Boolean isSubmitted = false;

    private WorkProcessStudentResponse workProcessStudentResponse;

    @Getter
    @Setter
    public static class WorkProcessStudentResponse {
        private Long id;

        @JsonFormat(pattern = "hh:mm dd/MM/yyy")
        private LocalDateTime createdDate;

        private String title;

        private String content;

        private String file;

        private Float percent;

        @JsonFormat(pattern = "hh:mm dd/MM/yyy")
        private LocalDateTime replayDate;

        private String replay;

        private StudentRegis studentRegis;

    }
}
