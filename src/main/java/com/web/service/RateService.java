package com.web.service;

import com.web.entity.Rate;
import com.web.repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateService {

    @Autowired
    private RateRepository rateRepository;

    public Rate save(Rate rate){
        rateRepository.save(rate);
        return rate;
    }
}
