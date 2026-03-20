package com.web.mapper;

import com.web.dto.request.DocumentRequest;
import com.web.entity.Document;
import com.web.entity.DocumentDetail;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    @Autowired
    private ModelMapper mapper;

    public Document requestToEntity(DocumentRequest request){
        Document document = mapper.map(request, Document.class);
        return document;
    }

    public DocumentDetail requestToEntity(DocumentRequest.Detail request){
        DocumentDetail document = mapper.map(request, DocumentDetail.class);
        return document;
    }
}
