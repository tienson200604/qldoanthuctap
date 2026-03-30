package com.web.repository;

import com.web.entity.StudentRegis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

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

    @Query("""
        SELECT s FROM StudentRegis s
        WHERE s.semesterTeacher.semesterType.semester.id = :semesterId
          AND (:teacherId IS NULL OR s.semesterTeacher.teacher.id = :teacherId)
          AND (:keyword IS NULL OR LOWER(s.student.fullname) LIKE LOWER(CONCAT('%',:keyword,'%'))
                                OR LOWER(s.student.code) LIKE LOWER(CONCAT('%',:keyword,'%')))
          AND (:classname IS NULL OR LOWER(s.student.classname) LIKE LOWER(CONCAT('%',:classname,'%')))
        """)
    List<StudentRegis> findScoreByFilter(
        @Param("semesterId") Long semesterId,
        @Param("teacherId") Long teacherId,
        @Param("keyword") String keyword,
        @Param("classname") String classname
    );
}
