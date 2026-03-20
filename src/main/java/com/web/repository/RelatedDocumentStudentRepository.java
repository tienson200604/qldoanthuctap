package com.web.repository;

import com.web.entity.RelatedDocumentStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface RelatedDocumentStudentRepository extends JpaRepository<RelatedDocumentStudent, Long> {

    @Modifying
    @Transactional
    @Query("delete from RelatedDocumentStudent p where p.relatedDocuments.id = ?1")
    int deleteByRelatedDocuments(Long productId);

    @Query("select r from RelatedDocumentStudent r where r.studentRegis.id = ?1 and r.relatedDocuments.id = ?2")
    Optional<RelatedDocumentStudent> findByStudentAndRelatedDocuments(Long id, Long id1);

    @Query("select r from RelatedDocumentStudent r where r.studentRegis.id = ?1 and r.relatedDocuments.semesterTeacher.id = ?2")
    List<RelatedDocumentStudent> findByStudentRegisAndSemesterTeacherId(Long id, Long semesterTeacherId);

    @Query("select r from RelatedDocumentStudent r where r.relatedDocuments.id = ?1")
    List<RelatedDocumentStudent> findByRelatedDocumentId(Long id);

    @Query("select r from RelatedDocumentStudent r where r.studentRegis.id = ?1")
    List<RelatedDocumentStudent> findByStudentRegisId(Long studentRegisId);
}
