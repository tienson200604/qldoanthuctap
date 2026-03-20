package com.web.service;

import com.web.dto.request.ScoreRatioRequest;
import com.web.entity.ScoreRatio;
import com.web.entity.Semester;
import com.web.exception.MessageException;
import com.web.repository.ScoreRatioRepository;
import com.web.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ScoreRatioService {

    @Autowired
    private ScoreRatioRepository scoreRatioRepository;

    @Autowired
    private SemesterRepository semesterRepository;


    public ScoreRatio save(ScoreRatioRequest request){

        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new MessageException("Không tìm thấy học kỳ"));

        ScoreRatio scoreRatio;

        Float totalPercent = scoreRatioRepository.sumPercentBySemester(request.getSemesterId());

        if(request.getId() == null){

            if(scoreRatioRepository.existsByNameAndSemesterId(request.getName(), request.getSemesterId())){
                throw new MessageException("Tên tỷ lệ điểm đã tồn tại trong học kỳ");
            }

            if(totalPercent + request.getPercent() > 100){
                throw new MessageException("Tổng tỷ lệ điểm trong học kỳ không được vượt quá 100%, chỉ còn: "+(100 - totalPercent)+" %");
            }


            scoreRatio = new ScoreRatio();
        }
        else{

            scoreRatio = scoreRatioRepository.findById(request.getId())
                    .orElseThrow(() -> new MessageException("Không tìm thấy tỷ lệ điểm"));

            if(!scoreRatio.getName().equals(request.getName())){
                if(scoreRatioRepository.existsByNameAndSemesterId(request.getName(), request.getSemesterId())){
                    throw new MessageException("Tên tỷ lệ điểm đã tồn tại trong học kỳ");
                }
            }

            Float newTotal = totalPercent - scoreRatio.getPercent() + request.getPercent();

            if(newTotal > 100){
                Float remain = 100 - (totalPercent - scoreRatio.getPercent());
                throw new MessageException("Tổng tỷ lệ điểm trong học kỳ không được vượt quá 100%, chỉ còn: "+remain+" %");
            }
        }

        scoreRatio.setName(request.getName());
        scoreRatio.setPercent(request.getPercent());
        scoreRatio.setSemester(semester);

        return scoreRatioRepository.save(scoreRatio);
    }


    public Map<String,String> delete(Long id){
        try{
            scoreRatioRepository.deleteById(id);
            return Map.of("message","Xóa tỷ lệ điểm thành công");
        }
        catch(Exception e){
            throw new MessageException("Không thể xóa tỷ lệ điểm");
        }
    }


    public ScoreRatio findById(Long id){
        return scoreRatioRepository.findById(id)
                .orElseThrow(() -> new MessageException("Không tìm thấy tỷ lệ điểm"));
    }


    public List<ScoreRatio> findAll(Long semesterId){
        if(semesterId == null){
            return scoreRatioRepository.findAll();
        }
        else{
            return scoreRatioRepository.findBySemesterId(semesterId);
        }
    }

}