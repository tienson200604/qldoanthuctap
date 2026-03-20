package com.web.api;

import com.web.dto.request.DocumentRequest;
import com.web.entity.Document;
import com.web.entity.Rate;
import com.web.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rate")
public class RateApi {

    @Autowired
    private RateService rateService;

    @PostMapping("/student/create-update")
    public ResponseEntity<?> save(@RequestBody Rate rate){
        Rate result = rateService.save(rate);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

}
