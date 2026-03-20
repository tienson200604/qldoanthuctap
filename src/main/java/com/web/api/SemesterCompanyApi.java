package com.web.api;

import com.web.dto.request.SemesterCompanyRequest;
import com.web.entity.SemesterCompany;
import com.web.service.SemesterCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/semester-company")
public class SemesterCompanyApi {

    @Autowired
    private SemesterCompanyService semesterCompanyService;

    @PostMapping("/admin/create")
    public Map<String, String> create(@RequestBody SemesterCompanyRequest request){
        return semesterCompanyService.save(request);
    }

    @PostMapping("/admin/update")
    public Map<String, String> update(@RequestBody SemesterCompanyRequest.SemesterCompanyUpdate request){
        return semesterCompanyService.update(request);
    }

    @GetMapping("/public/find-by-id")
    public SemesterCompany findById(@RequestParam Long id){
        return semesterCompanyService.findById(id);
    }

    @DeleteMapping("/admin/delete")
    public Map<String,String> delete(@RequestParam Long id){
        return semesterCompanyService.delete(id);
    }

    @GetMapping("/public/find-all")
    public Page<SemesterCompany> findAll(Pageable pageable,
                                         @RequestParam Long semesterId,
                                         @RequestParam(required = false) String search){
        return semesterCompanyService.findAll(search, semesterId, pageable);
    }
}