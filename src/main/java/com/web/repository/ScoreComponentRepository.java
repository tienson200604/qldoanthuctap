package com.web.repository;

import com.web.entity.ScoreComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScoreComponentRepository extends JpaRepository<ScoreComponent, Long> {

    @Query("select s from ScoreComponent s where s.studentRegis.id = ?1")
    List<ScoreComponent> findByStudentRegisId(Long studentRegisId);

    @Query("select s from ScoreComponent s where s.studentRegis.id = ?1 and s.scoreRatioId = ?2")
    Optional<ScoreComponent> findByStudentRegisIdAndScoreRatioId(Long studentRegisId, Long scoreRatioId);


}
