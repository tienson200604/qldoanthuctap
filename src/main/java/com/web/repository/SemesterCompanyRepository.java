package com.web.repository;

import com.web.entity.SemesterCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SemesterCompanyRepository extends JpaRepository<SemesterCompany, Long> {

    @Query("select s from SemesterCompany s where lower(s.company.name) like lower(?1) and s.semester.id = ?2")
    Page<SemesterCompany> findByParamAndSemester(String search, Long semesterId, Pageable pageable);

    Page<SemesterCompany> findBySemesterId(Long semesterId, Pageable pageable);

    @Query("select s from SemesterCompany s where s.semester.id = ?1")
    List<SemesterCompany> findBySemesterId(Long semesterId);

    @Query("select s from SemesterCompany s where s.semester.isActive = true")
    List<SemesterCompany> findBySemesterActive();

    @Query("select s from SemesterCompany s where s.semester.id = ?1 and s.company.id = ?2")
    Optional<SemesterCompany> findBySemesterAndCompany(Long semesterId, Long companyId);
}