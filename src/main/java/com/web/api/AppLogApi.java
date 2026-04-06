package com.web.api;

import com.web.entity.AppLog;
import com.web.enums.LogLevel;
import com.web.service.AppLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin
public class AppLogApi {

    @Autowired
    private AppLogService appLogService;

    @GetMapping("/admin/find-all")
    public ResponseEntity<Page<AppLog>> findAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) LogLevel logLevel,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            Pageable pageable) {
        Page<AppLog> result = appLogService.findAll(keyword, logLevel, from, to, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/all/my-logs")
    public ResponseEntity<Page<AppLog>> findMyLogs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) LogLevel logLevel,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            Pageable pageable) {
        Page<AppLog> result = appLogService.findMyLogs(keyword, logLevel, from, to, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
