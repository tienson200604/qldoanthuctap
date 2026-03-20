package com.web.api;

import com.web.dto.request.WorkProcessRequest;
import com.web.dto.response.WorkProcessResponse;
import com.web.entity.WorkProcess;
import com.web.service.WorkProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/work-process")
public class WorkProcessApi {

    @Autowired
    private WorkProcessService workProcessService;


    @PostMapping("/teacher/create-update")
    public WorkProcess createUpdate(@RequestBody WorkProcessRequest request){
        return workProcessService.save(request);
    }

    @GetMapping("/student-teacher/find-all")
    public Page<WorkProcess> findAll(Pageable pageable,
                                     @RequestParam Long semesterTeacherId,
                                     @RequestParam(required = false) String search){
        return workProcessService.findAll(search, semesterTeacherId, pageable);
    }

    @GetMapping("/student/find-all")
    public Page<WorkProcessResponse> findAllByStudent(Pageable pageable,
                                                      @RequestParam Long semesterTeacherId,
                                                      @RequestParam(required = false) String search){
        return workProcessService.findAllByStudent(search, semesterTeacherId, pageable);
    }

    @GetMapping("/student-teacher/find-by-id")
    public WorkProcess findById(@RequestParam Long id){
        return workProcessService.findById(id);
    }

    @DeleteMapping("/teacher/delete")
    public Map<String,String> delete(@RequestParam Long id){
        return workProcessService.delete(id);
    }

}