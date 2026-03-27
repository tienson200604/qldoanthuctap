package com.web.api;

import com.web.dto.response.StudentScoreResponse;
import com.web.entity.ScoreComponent;
import com.web.service.ScoreComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/score_componentApi")
public class ScoreComponentApi {

    @Autowired
    private ScoreComponentService scoreComponentService;

    @PostMapping("/teacher/create-update")
    public ScoreComponent createUpdate(@RequestBody ScoreComponent scoreComponent){
        return scoreComponentService.save(scoreComponent);
    }

    @GetMapping("/teacher/find-by-studentRegis")
    public Map<String, Object> findAll(@RequestParam Long studentRegisId){
        return scoreComponentService.getByStudentRegis(studentRegisId);
    }

    @GetMapping("/admin/find-by-studentRegis")
    public Map<String, Object> findAllAdmin(@RequestParam Long studentRegisId){
        return scoreComponentService.getByStudentRegis(studentRegisId);
    }

    @DeleteMapping("/teacher/delete")
    public void delete(@RequestParam Long id){
        scoreComponentService.delete(id);
    }

    @GetMapping("/admin/student-scores")
    public List<StudentScoreResponse> getStudentScores(
            @RequestParam Long semesterId,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String className) {
        return scoreComponentService.getStudentScores(semesterId, teacherId, keyword, className);
    }

    @GetMapping("/admin/export-excel")
    public void exportExcel(
            @RequestParam Long semesterId,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String className,
            HttpServletResponse response) throws IOException {
        scoreComponentService.exportStudentScoresToExcel(semesterId, teacherId, keyword, className, response);
    }
}
