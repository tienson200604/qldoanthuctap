package com.web.api;

import com.web.dto.request.SemesterCompanyRequest;
import com.web.entity.SemesterTeacher;
import com.web.entity.User;
import com.web.service.SemesterCompanyService;
import com.web.service.SemesterTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/semester-teacher")
public class SemesterTeacherApi {

    @Autowired
    private SemesterTeacherService semesterTeacherService;

    @GetMapping("/admin/find-by-type")
    public List<SemesterTeacher> findByType(@RequestParam Long semesterTypeId){
        return semesterTeacherService.findBySesType(semesterTypeId);
    }

    @DeleteMapping("/admin/delete")
    public void delete(@RequestParam Long id){
        semesterTeacherService.delete(id);
    }
}
