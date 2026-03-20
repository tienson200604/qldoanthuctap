package com.web.repository;

import com.web.entity.WorkProcessStudent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface WorkProcessStudentRepository extends JpaRepository<WorkProcessStudent, Long> {

    @Query("select w from WorkProcessStudent w where lower(w.title) like lower(?1) and w.workProcess.id = ?2")
    Page<WorkProcessStudent> findByParamAndWorkProcess(String search, Long workProcessId, Pageable pageable);

    Page<WorkProcessStudent> findByWorkProcessId(Long workProcessId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("delete from WorkProcessStudent p where p.workProcess.id = ?1")
    int deleteByWorkProcess(Long workProcessId);

    @Query("select w from WorkProcessStudent w where w.studentRegis.id = ?1")
    List<WorkProcessStudent> findByStudentRegisId(Long studentRegisId);

    @Query("select w from WorkProcessStudent w where w.studentRegis.id = ?1 and w.workProcess.semesterTeacher.id = ?2")
    List<WorkProcessStudent> findByStudentRegisId(Long studentRegisId, Long semesterTeacherId);

    @Query("select w from WorkProcessStudent w where w.workProcess.id = ?1")
    List<WorkProcessStudent> findByWorkProcess(Long workProcessId);

    @Query("select w from WorkProcessStudent w where w.studentRegis.id = ?1 and w.workProcess.id = ?2")
    Optional<WorkProcessStudent> findByStudentAndWorkProcess(Long id, Long id1);
}