package com.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.entity.RelatedDocuments;
import com.web.entity.SemesterTeacher;
import com.web.entity.StudentRegis;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
public class RelatedDocumentsResponse {

    private Long id;

    private String name;

    private String description;

    private Integer onTimeCount = 0;

    private Integer outTimeCount = 0;

    private LocalDateTime createdDate;

    private LocalDateTime deadline;

    private SemesterTeacher semesterTeacher;

    private Boolean isSubmitted = false;

    private RelatedDocumentStudentResponse relatedDocumentStudentResponse;

    @Getter
    @Setter
    public static class RelatedDocumentStudentResponse{
        private Long id;

        @JsonFormat(pattern = "hh:mm dd/MM/yyyy")
        private LocalDateTime createdDate;

        private String fileUrl;

        private String fileName;

        private String fileType;

        private StudentRegis studentRegis;

    }
}
