package com.web.api;

import com.web.dto.response.RelatedDocumentsResponse;
import com.web.entity.RelatedDocuments;
import com.web.entity.ScoreComponent;
import com.web.service.RelatedDocumentsService;
import com.web.service.ScoreComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/teacher/delete")
    public void delete(@RequestParam Long id){
        scoreComponentService.delete(id);
    }
}
