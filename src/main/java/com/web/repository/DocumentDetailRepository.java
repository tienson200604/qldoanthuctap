package com.web.repository;

import com.web.entity.Document;
import com.web.entity.DocumentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentDetailRepository extends JpaRepository<DocumentDetail, Long> {
}
