package com.web.api;

import com.web.dto.request.BlogRequest;
import com.web.dto.request.DocumentRequest;
import com.web.dto.response.BlogResponse;
import com.web.entity.Document;
import com.web.enums.DocumentStatus;
import com.web.service.BlogService;
import com.web.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/document")
public class DocumentApi {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/admin-teacher/create-update")
    public ResponseEntity<?> save(@RequestBody DocumentRequest request){
        Document result = documentService.saveOrUpdate(request);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/public/search-public")
    public Page<Document> getDocumentPublic(@RequestParam(required = false) String search,
                                        @RequestParam(required = false) Long categoryId,Pageable pageable){
        Page<Document> page = documentService.searchAll(search, categoryId,  pageable);
        return page;
    }

    @GetMapping("/admin/all")
    public Page<Document> getByAdmin(@RequestParam(required = false) String search,@RequestParam(required = false) DocumentStatus status,
                                        @RequestParam(required = false) Long categoryId,Pageable pageable){
        Page<Document> page = documentService.searchAllByAdmin(search, categoryId, status,  pageable);
        return page;
    }

    @GetMapping("/teacher/my-document")
    public Page<Document> myDocument(@RequestParam(required = false) String search,@RequestParam(required = false) DocumentStatus status,
                                        @RequestParam(required = false) Long categoryId,Pageable pageable){
        Page<Document> page = documentService.myDocument(search, categoryId, status,  pageable);
        return page;
    }

//    @GetMapping("/teacher/my-document")
//    public Page<Document> getMyDocument(Pageable pageable){
//        Page<Document> page = documentService.myDocument(pageable);
//        return page;
//    }

    @GetMapping("/public/find-by-id")
    public Document getDocumentDetailPublic(@RequestParam Long id){
        Document result = documentService.findByIdPublic(id);
        return result;
    }

    @PostMapping("/public/up-download")
    public void upDownload(@RequestParam Long id){
        documentService.upDownload(id);
    }

    @GetMapping("/admin-teacher/find-by-id")
    public Document getDocumentDetailPrivate(@RequestParam Long id){
        Document result = documentService.findByPrivate(id);
        return result;
    }

    @DeleteMapping("/admin-teacher/delete")
    public Map<String, String> deleteByID(@RequestParam("id") Long id){
        Map<String, String> map = documentService.delete(id);
        return map;
    }

    @DeleteMapping("/admin-teacher/delete-detail")
    public Map<String, String> deleteDetail(@RequestParam Long idDetail){
        Map<String, String> map = documentService.deleteDetail(idDetail);
        return map;
    }

    @PostMapping("/admin/update-status")
    public Map<String, Object> updateStatus(@RequestParam Long id, @RequestParam DocumentStatus status){
        Map<String, Object> map = documentService.updateStatus(id, status);
        return map;
    }


    @GetMapping("/admin-teacher/document-status")
    public List<DocumentStatus> getAllStatus(){
        return Arrays.asList(DocumentStatus.values());
    }

}
