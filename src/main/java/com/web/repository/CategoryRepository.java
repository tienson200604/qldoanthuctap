package com.web.repository;

import com.web.entity.Category;
import com.web.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where c.categoryType = ?1")
    List<Category> findByCategoryType(CategoryType categoryType);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
