package com.web.api;

import com.web.dto.request.ScoreRatioRequest;
import com.web.entity.ScoreRatio;
import com.web.service.ScoreRatioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/score-ratio")
public class ScoreRatioApi {

    @Autowired
    private ScoreRatioService scoreRatioService;

    @PostMapping("/admin/create-update")
    public ScoreRatio createUpdate(@RequestBody ScoreRatioRequest request){
        return scoreRatioService.save(request);
    }


    @GetMapping("/public/find-by-semesterId")
    public List<ScoreRatio> findAll(@RequestParam Long semesterId){
        return scoreRatioService.findAll(semesterId);
    }


    @GetMapping("/public/find-by-id")
    public ScoreRatio findById(@RequestParam Long id){
        return scoreRatioService.findById(id);
    }


    @DeleteMapping("/admin/delete")
    public Map<String,String> delete(@RequestParam Long id){
        return scoreRatioService.delete(id);
    }
}