package com.web.repository;

import com.web.entity.StudentRegis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRegisRepository extends JpaRepository<StudentRegis, Long> {

    @Query("select s from StudentRegis s where s.student.id = ?1 and s.semesterTeacher.semesterType.semester.id = ?2")
    Optional<StudentRegis> findByStudentAndSemester(Long studentId, Long semesterId);

    @Query("select s from StudentRegis s where s.student.id = ?1 and s.semesterTeacher.id = ?2")
    Optional<StudentRegis> findByStudentAndSemesterTeacher(Long studentId, Long semesterTeacherId);

    @Query("select s from StudentRegis s where s.student.id = ?1")
    List<StudentRegis> findByUser(Long id);

    @Query("select s from StudentRegis s where s.semesterTeacher.semesterType.id = ?1")
        List<StudentRegis> findBySesType(Long sesTeacherId);

    @Query("select s from StudentRegis s where s.semesterTeacher.semesterType.semester.id = ?1 and s.semesterTeacher.teacher.id = ?2")
    List<StudentRegis> findBySemesterIdAndTeacherId(Long semesterId, Long teacherId);

    @Query("select s from StudentRegis s where s.semesterTeacher.id = ?1")
    List<StudentRegis> findBySemesterTeacher(Long semesterTeacherId);
}
