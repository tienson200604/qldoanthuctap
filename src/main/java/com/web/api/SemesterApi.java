package com.web.api;

import com.web.entity.Semester;
import com.web.service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/semester")
public class SemesterApi {

    @Autowired
    private SemesterService service;

    @PostMapping("/admin/create-update")
    public ResponseEntity<?> create(@RequestBody Semester semester) {
        Semester result = service.save(semester);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/public/all")
    public ResponseEntity<List<Semester>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/public/find-by-id")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<?> delete(@RequestParam Long id) {
        Map<String, String> map = service.delete(id);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
