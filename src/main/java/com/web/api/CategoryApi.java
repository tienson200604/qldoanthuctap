package com.web.api;

import com.web.entity.Category;
import com.web.enums.CategoryType;
import com.web.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/category")
public class CategoryApi {

    @Autowired
    private CategoryService service;

    @PostMapping("/admin/create-update")
    public ResponseEntity<?> save(@RequestBody Category category) {
        return ResponseEntity.ok(service.saveOrUpdate(category));
    }

    @GetMapping("/public/find-by-id")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/public/all")
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/public/all-by-type")
    public ResponseEntity<List<Category>> getByType(@RequestParam CategoryType type) {
        return ResponseEntity.ok(service.findByType(type));
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<?> delete(@RequestParam Long id) {
        Map<String, String> map = service.delete(id);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
