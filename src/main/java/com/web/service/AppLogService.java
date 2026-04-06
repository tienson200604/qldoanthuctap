package com.web.service;

import com.web.entity.AppLog;
import com.web.entity.User;
import com.web.enums.LogLevel;
import com.web.repository.AppLogRepository;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AppLogService {

    @Autowired
    private AppLogRepository appLogRepository;

    @Autowired
    private UserUtils userUtils;

    @org.springframework.transaction.annotation.Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public void addLog(String content, LogLevel level) {
        try {
            AppLog log = new AppLog();
            log.setActionContent(content);
            log.setLogLevel(level);
            log.setCreatedDate(LocalDateTime.now());
            try {
                User user = userUtils.getUserWithAuthority();
                log.setUser(user);
            } catch (Exception e) {
                log.setUser(null);
            }
            appLogRepository.save(log);
        } catch (Exception e) {
            System.err.println("Không thể ghi nhật ký hệ thống: " + e.getMessage());
        }
    }

    public Page<AppLog> findAll(String keyword, LogLevel logLevel, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        return appLogRepository.findAllLogs(keyword, logLevel, from, to, pageable);
    }
}
