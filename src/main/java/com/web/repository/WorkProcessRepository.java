package com.web.repository;

import com.web.entity.WorkProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkProcessRepository extends JpaRepository<WorkProcess, Long> {

    @Query("select w from WorkProcess w where lower(w.title) like lower(?1) and w.semesterTeacher.id = ?2")
    Page<WorkProcess> findByParamAndTeacher(String search, Long semesterTeacherId, Pageable pageable);

    Page<WorkProcess> findBySemesterTeacherId(Long semesterTeacherId, Pageable pageable);

    @Query("select w from WorkProcess w where w.semesterTeacher.id = ?1")
    List<WorkProcess> findBySemesterTeacherId(Long semesterTeacherId);
}