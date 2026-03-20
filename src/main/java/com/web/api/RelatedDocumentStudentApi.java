package com.web.api;

import com.web.entity.RelatedDocumentStudent;
import com.web.entity.RelatedDocuments;
import com.web.service.RelatedDocumentStudentService;
import com.web.service.RelatedDocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/related_document_student")
public class RelatedDocumentStudentApi {

    @Autowired
    private RelatedDocumentStudentService relatedDocumentStudentService;

    @PostMapping("/student/create")
    public RelatedDocumentStudent createUpdate(@RequestBody RelatedDocumentStudent request){
        return relatedDocumentStudentService.save(request);
    }

}
