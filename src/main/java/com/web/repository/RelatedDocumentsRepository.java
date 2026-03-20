package com.web.repository;

import com.web.entity.RelatedDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RelatedDocumentsRepository extends JpaRepository<RelatedDocuments, Long > {

    @Query("select r from RelatedDocuments r where r.name = ?1 and r.semesterTeacher.id = ?2")
    Optional<RelatedDocuments> findByNameAndSemesterTeacher(String name, Long semesterTeacherId);

    @Query("select r from RelatedDocuments r where r.name = ?1 and r.semesterTeacher.id = ?2 and r.id <> ?3")
    Optional<RelatedDocuments> findByNameAndSemesterTeacher(String name, Long semesterTeacherId, Long id);

    @Query("select r from RelatedDocuments r where r.semesterTeacher.id = ?1")
    List<RelatedDocuments> findBySemesterTeacher(Long semesterTeacherId);

    @Query("select count(r.id) from RelatedDocuments r where r.semesterTeacher.id = ?1")
    Long countBySemesterTeacher(Long semesterTeacherId);
}
