package com.web.repository;

import com.web.entity.AppLog;
import com.web.enums.LogLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface AppLogRepository extends JpaRepository<AppLog, Long> {

    @Query("SELECT l FROM AppLog l WHERE " +
            "(:keyword IS NULL OR LOWER(l.actionContent) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(l.user.fullname) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(l.user.username) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:logLevel IS NULL OR l.logLevel = :logLevel) " +
            "AND (:from IS NULL OR l.createdDate >= :from) " +
            "AND (:to IS NULL OR l.createdDate <= :to)")
    Page<AppLog> findAllLogs(String keyword, LogLevel logLevel, LocalDateTime from, LocalDateTime to, Pageable pageable);

    @Query("SELECT l FROM AppLog l WHERE l.user.id = :userId " +
            "AND (:keyword IS NULL OR LOWER(l.actionContent) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:logLevel IS NULL OR l.logLevel = :logLevel) " +
            "AND (:from IS NULL OR l.createdDate >= :from) " +
            "AND (:to IS NULL OR l.createdDate <= :to)")
    Page<AppLog> findMyLogs(@Param("userId") Long userId,
                            @Param("keyword") String keyword,
                            @Param("logLevel") LogLevel logLevel,
                            @Param("from") LocalDateTime from,
                            @Param("to") LocalDateTime to,
                            Pageable pageable);
}
