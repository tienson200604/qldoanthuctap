package com.web.dto.request;

import com.web.enums.DocumentStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DocumentRequest {

    private Long id;

    private String name;

    private String linkImage;

    private String description;

    private Long categoryId;

    private DocumentStatus status;

    private List<Detail> details = new ArrayList<>();

    @Getter
    @Setter
    public static class Detail{

        private Long id;

        private String name;

        private String fileType;

        private String linkFile;

    }
}
