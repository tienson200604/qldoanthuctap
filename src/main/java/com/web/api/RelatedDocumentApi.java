package com.web.api;

import com.web.dto.response.RelatedDocumentsResponse;
import com.web.entity.RelatedDocuments;
import com.web.service.RelatedDocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/related_document")
public class RelatedDocumentApi {

    @Autowired
    private RelatedDocumentsService relatedDocumentsService;

    @PostMapping("/teacher/create-update")
    public RelatedDocuments createUpdate(@RequestBody RelatedDocuments request){
        return relatedDocumentsService.save(request);
    }

    @GetMapping("/student-teacher/find-all")
    public List<RelatedDocuments> findAll(@RequestParam Long semesterTeacherId){
        return relatedDocumentsService.findBySemesterTeacher(semesterTeacherId);
    }

    @GetMapping("/student/find-all")
    public List<RelatedDocumentsResponse> findAllByStudent(@RequestParam Long semesterTeacherId){
        return relatedDocumentsService.findBySemesterTeacherForStudent(semesterTeacherId);
    }

    @GetMapping("/student-teacher/find-by-id")
    public RelatedDocuments findById(@RequestParam Long id){
        return relatedDocumentsService.findById(id);
    }

    @DeleteMapping("/teacher/delete")
    public void delete(@RequestParam Long id){
        relatedDocumentsService.delete(id);
    }
}
