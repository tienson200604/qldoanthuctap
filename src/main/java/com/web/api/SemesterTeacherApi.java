package com.web.api;

import com.web.dto.request.SemesterTeacherAdminRequest;
import com.web.entity.SemesterTeacher;
import com.web.service.SemesterTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/semester-teacher")
public class SemesterTeacherApi {

    @Autowired
    private SemesterTeacherService semesterTeacherService;

    @GetMapping("/admin/find-by-type")
    public List<SemesterTeacher> findByType(@RequestParam Long semesterTypeId){
        return semesterTeacherService.findBySesType(semesterTypeId);
    }

    @GetMapping("/admin/search")
    public List<SemesterTeacher> search(@RequestParam(required = false) Long semesterId,
                                        @RequestParam(required = false) Long semesterTypeId,
                                        @RequestParam(required = false) String keyword){
        return semesterTeacherService.searchAdmin(semesterId, semesterTypeId, keyword);
    }

    @GetMapping("/admin/{id}")
    public SemesterTeacher findById(@PathVariable Long id) {
        return semesterTeacherService.findById(id);
    }

    @PostMapping("/admin/create-update")
    public SemesterTeacher createUpdate(@RequestBody SemesterTeacherAdminRequest request) {
        return semesterTeacherService.saveAdmin(request);
    }

    @DeleteMapping("/admin/delete")
    public void delete(@RequestParam Long id){
        semesterTeacherService.delete(id);
    }
}
