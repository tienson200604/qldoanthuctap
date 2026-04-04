package com.web.service;

import com.web.entity.Rate;
import com.web.entity.StudentRegis;
import com.web.repository.RateRepository;
import com.web.repository.StudentRegisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateService {

    @Autowired
    private RateRepository rateRepository;

    @Autowired
    private StudentRegisRepository studentRegisRepository;

    @Autowired
    private NotificationService notificationService;

    public Rate save(Rate rate){
        double avg = (rate.getQ1() + rate.getQ2() + rate.getQ3()) / 3.0;
        rate.setAvgScore(avg);
        rateRepository.save(rate);
        StudentRegis studentRegis = studentRegisRepository.findById(rate.getStudentRegis().getId()).get();
        notificationService.save("Đánh giá mới", "/admin/rate",
                "Có đánh giá thực tập mới từ sinh viên: "+studentRegis.getStudent().getFullname()+" ("+Math.round(avg * 10.0) / 10.0+" sao)");
        return rate;
    }

    public java.util.List<Rate> findAll(){
        return rateRepository.findAll();
    }
}
