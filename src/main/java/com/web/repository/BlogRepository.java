package com.web.repository;

import com.web.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog,Long> {

    @Query(value = "select b.* from blog b order by b.num_view desc limit 7", nativeQuery = true)
    public List<Blog> bestView();

    @Query(value = "select b.* from blog b order by b.id desc limit 10", nativeQuery = true)
    List<Blog> newBlog();

    @Query(value = "select b.* from blog b where b.id != ?1 order by b.id desc limit 10", nativeQuery = true)
    List<Blog> newBlogAndNotId(Long id);

    Page<Blog> findByCategoryId(Long id, Pageable pageable);

    @Query("select b from Blog b where lower(b.title) like lower(concat('%',:keyword,'%'))")
    Page<Blog> search(String keyword, Pageable pageable);

    @Query("select b from Blog b where lower(b.title) like lower(concat('%',:keyword,'%'))")
    List<Blog> search(String keyword);
}
