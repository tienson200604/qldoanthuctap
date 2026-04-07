package com.web.repository;

import com.web.entity.SemesterTeacher;
import com.web.enums.InternshipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SemesterTeacherRepository extends JpaRepository<SemesterTeacher, Long> {

    @Query("select s from SemesterTeacher s where s.teacher.id = ?1 and s.semesterType.id = ?2")
    Optional<SemesterTeacher> findByTeacherAndSemesterType(Long teacherId, Long id);

    @Query("select s from SemesterTeacher s where s.teacher.id = ?1")
    List<SemesterTeacher> findByTeacher(Long teacherId);

    @Query("select s from SemesterTeacher s where s.semesterType.id = ?1")
    List<SemesterTeacher> findBySesType(Long sesTypeId);

    @Query("select s from SemesterTeacher s where s.semesterType.type = ?1 and s.semesterType.semester.isActive = true")
    List<SemesterTeacher> findByTypeAndSemesterActive(InternshipType type);

    @Query("select s from SemesterTeacher s where s.semesterType.semester.isActive = true")
    List<SemesterTeacher> findByTypeAndSemesterActive();

    @Query("""
            select s from SemesterTeacher s
            where (?1 is null or s.semesterType.semester.id = ?1)
            and (?2 is null or s.semesterType.id = ?2)
            and (
                ?3 is null
                or lower(s.teacher.fullname) like lower(concat('%', ?3, '%'))
                or lower(s.teacher.code) like lower(concat('%', ?3, '%'))
                or lower(s.projectName) like lower(concat('%', ?3, '%'))
            )
            order by s.semesterType.semester.isActive desc, s.semesterType.semester.id desc, s.teacher.fullname asc
            """)
    List<SemesterTeacher> searchAdmin(Long semesterId, Long semesterTypeId, String keyword);
}
