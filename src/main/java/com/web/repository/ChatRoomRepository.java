package com.web.repository;

import com.web.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {


    @Query("""
        SELECT c FROM ChatRoom c
        WHERE c.semesterTeacherId = :semesterTeacherId
        ORDER BY c.id desc 
    """)
    Page<ChatRoom> findByRoom(@Param("semesterTeacherId") Long semesterTeacherId, Pageable pageable);
}
