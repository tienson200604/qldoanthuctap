package com.web.api;

import com.web.dto.request.SemesterTypeRequest;
import com.web.dto.response.SemesterTypeResponse;
import com.web.entity.SemesterType;
import com.web.enums.DocumentStatus;
import com.web.enums.InternshipType;
import com.web.service.SemesterTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/semester-type")
public class SemesterTypeApi {

    @Autowired
    private SemesterTypeService semesterTypeService;


    @PostMapping("/admin/create-update")
    public SemesterType createUpdate(@RequestBody SemesterTypeRequest request){
        return semesterTypeService.save(request);
    }


    @GetMapping("/public/find-all")
    public List<SemesterType> findAll(@RequestParam Long semesterId){
        return semesterTypeService.findAll(semesterId);
    }


    @GetMapping("/public/find-by-id")
    public SemesterType findById(@RequestParam Long id){
        return semesterTypeService.findById(id);
    }

    @GetMapping("/public/find-by-type")
    public SemesterTypeResponse findByType(@RequestParam InternshipType type){
        return semesterTypeService.findByType(type);
    }


    @DeleteMapping("/admin/delete")
    public Map<String,String> delete(@RequestParam Long id){
        return semesterTypeService.delete(id);
    }

    @GetMapping("/admin/type")
    public List<InternshipType> getAllStatus(){
        return Arrays.asList(InternshipType.values());
    }

}