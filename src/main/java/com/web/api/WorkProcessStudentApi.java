package com.web.api;

import com.web.dto.request.WorkProcessRequest;
import com.web.dto.request.WorkProcessStudentRequest;
import com.web.entity.WorkProcess;
import com.web.entity.WorkProcessStudent;
import com.web.service.WorkProcessService;
import com.web.service.WorkProcessStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/work-process-student")
public class WorkProcessStudentApi {

    @Autowired
    private WorkProcessStudentService workProcessStudentService;

    @PostMapping("/student/create-update")
    public WorkProcessStudent createUpdate(@RequestBody WorkProcessStudentRequest request){
        return workProcessStudentService.save(request);
    }

    @PostMapping("/teacher/replay/{id}")
    public WorkProcessStudent replay(@PathVariable("id") Long id, @RequestBody String replay){
        return workProcessStudentService.replay(id, replay);
    }

    @GetMapping("/student-teacher/find-all")
    public Page<WorkProcessStudent> findAll(Pageable pageable,
                                     @RequestParam Long workProcessId,
                                     @RequestParam(required = false) String search){
        return workProcessStudentService.findAll(search, workProcessId, pageable);
    }

    @GetMapping("/student-teacher/find-by-id")
    public WorkProcessStudent findById(@RequestParam Long id){
        return workProcessStudentService.findById(id);
    }

    @GetMapping("/student-teacher/find-by-workprocess-id")
    public Map<String, Object> findByWorkProcess(@RequestParam Long workProcessId){
        return workProcessStudentService.findByWorkProcess(workProcessId);
    }

    @DeleteMapping("/student/delete")
    public Map<String,String> delete(@RequestParam Long id){
        return workProcessStudentService.delete(id);
    }

}
