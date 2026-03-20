package com.web.mapper;

import com.web.dto.response.RelatedDocumentsResponse;
import com.web.dto.response.WorkProcessResponse;
import com.web.entity.RelatedDocumentStudent;
import com.web.entity.RelatedDocuments;
import com.web.entity.WorkProcess;
import com.web.entity.WorkProcessStudent;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RelatedDocumentMapper {

    @Autowired
    private ModelMapper mapper;

    public RelatedDocumentsResponse entityToResponse(RelatedDocuments relatedDocuments){
        RelatedDocumentsResponse dto = mapper.map(relatedDocuments, RelatedDocumentsResponse.class);
        return dto;
    }

    public RelatedDocumentsResponse.RelatedDocumentStudentResponse entityToResponse(RelatedDocumentStudent relatedDocumentStudent){
        RelatedDocumentsResponse.RelatedDocumentStudentResponse dto = mapper.map(relatedDocumentStudent,RelatedDocumentsResponse.RelatedDocumentStudentResponse.class);
        return dto;
    }
}
