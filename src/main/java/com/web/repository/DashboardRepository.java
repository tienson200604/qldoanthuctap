package com.web.repository;

import com.web.entity.StudentRegis;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DashboardRepository extends JpaRepository<StudentRegis, Long> {

    @Query("SELECT COUNT(u) FROM User u WHERE u.authorities.name = 'ROLE_STUDENT'")
    long countStudent();

    @Query("SELECT COUNT(s) FROM StudentRegis s")
    long countStudentRegis();

    @Query("SELECT COUNT(c) FROM Company c")
    long countCompany();

    @Query("SELECT COUNT(st) FROM SemesterTeacher st")
    long countProject();

    @Query("SELECT COUNT(st) FROM Document st")
    long countDocument();

    @Query("SELECT COUNT(st) FROM Blog st")
    long countBlog();

    @Query(value = "select s.* from student_regis s order by s.total_score desc limit 10", nativeQuery = true)
    List<StudentRegis> topStudent();
}
