package com.web.repository;

import com.web.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long>, JpaSpecificationExecutor<Document> {

    @Query("select d from Document d where d.user.id = ?1")
    Page<Document> findByUserId(Long id, Pageable pageable);

    @Query("select d from Document d where d.category.id = ?1 and d.id <> ?2")
    List<Document> documentLq(Long categoryId, Long id);

    @Query("select d from Document d where (d.name like ?1 or d.category.name like ?1) and d.status = 'DANG_HIEN_THI'")
    List<Document> search(String search);
}
