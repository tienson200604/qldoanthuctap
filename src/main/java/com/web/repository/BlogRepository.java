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

    @Query(value = "select b from Blog b where b.targetRole in :roles order by b.id desc")
    List<Blog> newBlog(List<String> roles, org.springframework.data.domain.Pageable pageable);

    @Query(value = "select b.* from blog b where b.id != ?1 order by b.id desc limit 10", nativeQuery = true)
    List<Blog> newBlogAndNotId(Long id);

    @Query(value = "select b from Blog b where b.id != :id and b.targetRole in :roles order by b.id desc")
    List<Blog> newBlogAndNotId(Long id, List<String> roles, org.springframework.data.domain.Pageable pageable);

    Page<Blog> findByCategoryId(Long id, Pageable pageable);

    @Query("select b from Blog b where b.category.id = :id and b.targetRole in :roles")
    Page<Blog> findByCategoryIdAndTargetRoleIn(Long id, List<String> roles, Pageable pageable);

    @Query("select b from Blog b where b.targetRole in :roles")
    Page<Blog> findByTargetRoleIn(List<String> roles, Pageable pageable);

    @Query("select b from Blog b where lower(b.title) like lower(concat('%',:keyword,'%'))")
    Page<Blog> search(String keyword, Pageable pageable);

    @Query("select b from Blog b where (lower(b.title) like lower(concat('%',:keyword,'%'))) and b.targetRole in :roles")
    Page<Blog> searchAndTargetRoleIn(String keyword, List<String> roles, Pageable pageable);

    @Query("select b from Blog b where lower(b.title) like lower(concat('%',:keyword,'%')) or " +
            "lower(b.description) like lower(concat('%',:keyword,'%')) or " +
            "lower(b.content) like lower(concat('%',:keyword,'%'))")
    List<Blog> search(String keyword);
}
