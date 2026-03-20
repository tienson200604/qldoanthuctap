package com.web.api;

import com.web.dto.request.StudentRegisRequest;
import com.web.entity.Company;
import com.web.entity.StudentRegis;
import com.web.service.StudentRegisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-regis")
public class StudentRegisApi {

    @Autowired
    private StudentRegisService studentRegisService;

    @PostMapping("/student/create")
    public StudentRegis create(@RequestBody StudentRegisRequest request){
        return studentRegisService.create(request);
    }

    @PostMapping("/teacher/accept")
    public StudentRegis accept(@RequestParam Long id){
        return studentRegisService.acceptRequest(id);
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

}
