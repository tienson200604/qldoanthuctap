package com.web.repository;

import com.web.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RateRepository extends JpaRepository<Rate, Long> {

    @Query("select r from Rate r where r.studentRegis.id = ?1")
    Optional<Rate> findByStudentRegisId(Long studentRegisId);
}
