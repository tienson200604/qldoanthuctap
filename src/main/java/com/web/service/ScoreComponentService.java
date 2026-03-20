package com.web.service;

import com.web.entity.ScoreComponent;
import com.web.entity.ScoreRatio;
import com.web.entity.StudentRegis;
import com.web.exception.MessageException;
import com.web.repository.ScoreComponentRepository;
import com.web.repository.ScoreRatioRepository;
import com.web.repository.StudentRegisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScoreComponentService {

    @Autowired
    private ScoreComponentRepository scoreComponentRepository;

    @Autowired
    private ScoreRatioRepository scoreRatioRepository;

    @Autowired
    private StudentRegisRepository studentRegisRepository;

    @Transactional
    public ScoreComponent save(ScoreComponent scoreComponent){
        Long studentRegisId = null;
        if(scoreComponent.getId() != null){
            ScoreComponent ex = scoreComponentRepository.findById(scoreComponent.getId()).get();
            ex.setPoint(scoreComponent.getPoint());
            studentRegisId = ex.getStudentRegis().getId();
        }
        else{
            ScoreRatio scoreRatio = scoreRatioRepository.findById(scoreComponent.getScoreRatioId()).orElseThrow(() -> new MessageException("Không tìm thấy mã điểm"));
            Optional<ScoreComponent> check = scoreComponentRepository.findByStudentRegisIdAndScoreRatioId(scoreComponent.getStudentRegis().getId(), scoreComponent.getScoreRatioId());
            if(check.isPresent()){
                throw new MessageException("Đầu điểm này đã được chấm");
            }
            scoreComponent.setName(scoreRatio.getName());
            scoreComponent.setScoreRatioId(scoreRatio.getId());
            scoreComponent.setPercent(scoreRatio.getPercent());
            scoreComponentRepository.save(scoreComponent);
            studentRegisId = scoreComponent.getStudentRegis().getId();
        }
        updateScoreTotal(studentRegisId);
        return scoreComponent;
    }

    public Map<String, Object> getByStudentRegis(Long studentRegisId){
        StudentRegis studentRegis = studentRegisRepository.findById(studentRegisId).orElseThrow(()-> new MessageException("Không tìm thấy sinh viên đã đăng ký"));
        Map<String, Object> map = new HashMap<>();
        List<ScoreComponent> scoreComponents = scoreComponentRepository.findByStudentRegisId(studentRegisId);
        List<ScoreRatio> allScoreRatios = scoreRatioRepository.findBySemesterId(studentRegis.getSemesterTeacher().getSemesterType().getSemester().getId());

        // lấy danh sách ScoreRatio không nằm trong scoreComponents
        // Lấy danh sách scoreRatioId đã tồn tại
        Set<Long> scoredRatioIds = scoreComponents.stream()
                .map(ScoreComponent::getScoreRatioId)
                .collect(Collectors.toSet());

        // Lọc ra những ScoreRatio chưa có
        List<ScoreRatio> notScored = allScoreRatios.stream()
                .filter(r -> !scoredRatioIds.contains(r.getId()))
                .collect(Collectors.toList());
        map.put("notScored", notScored);
        map.put("scored", scoreComponents);
        return map;
    }

    @Transactional
    public Map<String, Object> delete(Long id){
        try {
            scoreComponentRepository.deleteById(id);
            updateScoreTotal(id);
            return Map.of("message","Xóa thành công");
        }
        catch (Exception e){
            throw new MessageException("Xóa thất bại");
        }
    }

    public void updateScoreTotal(Long studentRegisId){
        StudentRegis studentRegis = studentRegisRepository.findById(studentRegisId).get();
        List<ScoreComponent> list = scoreComponentRepository.findByStudentRegisId(studentRegisId);
        Float total = 0F;
        for(ScoreComponent s : list){
            total += s.getPoint() * s.getPercent() / 100;
        }
        studentRegis.setTotalScore(total);
        studentRegisRepository.save(studentRegis);
    }
}
