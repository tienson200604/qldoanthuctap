package com.web.service;

import com.web.entity.Category;
import com.web.enums.CategoryType;
import com.web.exception.MessageException;
import com.web.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category saveOrUpdate(Category category) {
        if (category.getId() == null) {
            if (categoryRepository.existsByName(category.getName())) {
                throw new RuntimeException("Danh mục đã tồn tại!");
            }
        } else {
            if (categoryRepository.existsByNameAndIdNot(category.getName(), category.getId())) {
                throw new RuntimeException("Tên danh mục đã bị trùng!");
            }
        }
        return categoryRepository.save(category);
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id){
        return categoryRepository.findById(id).orElseThrow(() -> new MessageException("Không tồn tại danh mục với id: "+id));
    }

    public List<Category> findByType(CategoryType type) {
        return categoryRepository.findByCategoryType(type);
    }

    public Map<String, String> delete(Long id) {
        try {
            categoryRepository.deleteById(id);
            return Map.of("message", "Xóa danh mục thành công");
        }
        catch (Exception e){
            throw new MessageException("Danh mục đã có dữ liệu liên kết, không thể xóa");
        }
    }
}
