package com.web.repository;

import com.web.entity.ScoreRatio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScoreRatioRepository extends JpaRepository<ScoreRatio, Long> {

    @Query("select s from ScoreRatio s where lower(s.name) like lower(?1)")
    Page<ScoreRatio> findByParam(String search, Pageable pageable);

    boolean existsByNameAndSemesterId(String name, Long semesterId);

    @Query("select s from ScoreRatio s where s.semester.id = ?1")
    List<ScoreRatio> findBySemesterId(Long semesterId);

    @Query("select coalesce(sum(s.percent),0) from ScoreRatio s where s.semester.id = ?1")
    Float sumPercentBySemester(Long semesterId);
}
