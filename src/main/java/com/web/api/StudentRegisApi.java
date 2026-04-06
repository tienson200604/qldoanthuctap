package com.web.api;

import com.web.dto.request.StudentRegisRequest;
import com.web.dto.response.StudentRegisAdminDto;
import com.web.entity.Company;
import com.web.entity.StudentRegis;
import com.web.enums.InternshipType;
import com.web.enums.StudentRegisStatus;
import com.web.service.StudentRegisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/student-regis")
public class StudentRegisApi {

    @Autowired
    private StudentRegisService studentRegisService;

    @PostMapping("/student/create")
    public StudentRegis create(@Valid @RequestBody StudentRegisRequest request){
        return studentRegisService.create(request);
    }

    @PostMapping("/teacher/accept")
    public StudentRegis accept(@RequestParam Long id){
        return studentRegisService.acceptRequest(id);
    }

    @PostMapping("/teacher/reject")
    public StudentRegis reject(@RequestParam Long id){
        return studentRegisService.rejectRequest(id);
    }

    @PostMapping("/teacher/complete")
    public StudentRegis completeByTeacher(@RequestParam Long id){
        return studentRegisService.completeByTeacher(id);
    }

    @GetMapping("/public/create")
    public void createss(){
        studentRegisService.createAll();
    }

    @GetMapping("/student/my-regis")
    public List<StudentRegis> myRegis(){
        return studentRegisService.myRegis();
    }

    @GetMapping("/teacher/findBySemester")
    public List<StudentRegis> findByTeacher(@RequestParam Long semesterId){
        return studentRegisService.findBySemesterId(semesterId);
    }

    @GetMapping("/student/findById")
    public StudentRegis findById(@RequestParam Long id){
        return studentRegisService.findById(id);
    }

    @GetMapping("/admin/search")
    public Page<StudentRegisAdminDto> searchAdmin(@RequestParam(required = false) String keyword,
                                                  @RequestParam(required = false) Long semesterId,
                                                  @RequestParam(required = false) InternshipType internshipType,
                                                  @RequestParam(required = false) StudentRegisStatus status,
                                                  Pageable pageable) {
        return studentRegisService.searchAdmin(keyword, semesterId, internshipType, status, pageable);
    }

    @GetMapping("/admin/export-excel")
    public ResponseEntity<byte[]> exportExcel(@RequestParam(required = false) String keyword,
                                              @RequestParam(required = false) Long semesterId,
                                              @RequestParam(required = false) InternshipType internshipType,
                                              @RequestParam(required = false) StudentRegisStatus status) {
        byte[] data = studentRegisService.exportAdminExcel(keyword, semesterId, internshipType, status);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment().filename("danh-sach-sinh-vien-thuc-tap.xlsx").build());
        return ResponseEntity.ok().headers(headers).body(data);
    }

    @PostMapping("/admin/complete")
    public StudentRegis completeByAdmin(@RequestParam Long id){
        return studentRegisService.completeByAdmin(id);
    }

}
