package com.web.service;

import com.web.dto.request.BlogRequest;
import com.web.dto.response.BlogResponse;
import com.web.entity.Blog;
import com.web.entity.Category;
import com.web.exception.MessageException;
import com.web.mapper.BlogMapper;
import com.web.repository.BlogRepository;
import com.web.repository.CategoryRepository;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.web.entity.User;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private NotificationService notificationService;

    public BlogResponse saveOrUpdate(BlogRequest blogRequest) {
        Category category = categoryRepository.findById(blogRequest.getCategoryId()).orElseThrow(()-> new MessageException("Không tìm thấy danh mục với id: "+blogRequest.getCategoryId()));
        Blog blog = blogMapper.requestToBlog(blogRequest);
        blog.setUser(userUtils.getUserWithAuthority());
        blog.setCategory(category);
        blog.setCreatedDate(new Date(System.currentTimeMillis()));
        blog.setCreatedTime(new Time(System.currentTimeMillis()));
        if(blog.getId() == null){
            blog.setNumView(0);
        }
        else{
            Blog ex = blogRepository.findById(blogRequest.getId()).orElseThrow(() -> new MessageException("Không tìm thấy bài viết"));
            blog.setNumView(ex.getNumView());
        }
        Blog result = blogRepository.save(blog);
        if(blogRequest.getId() == null){
            if (result.getTargetRole().equals("ALL")) {
                notificationService.saveToRole(com.web.utils.Contains.ROLE_STUDENT, "Bài viết mới", "/student/blog-detail?id="+result.getId(), "Có bài viết mới: "+result.getTitle());
                notificationService.saveToRole(com.web.utils.Contains.ROLE_TEACHER, "Bài viết mới", "/teacher/blog-detail?id="+result.getId(), "Có bài viết mới: "+result.getTitle());
            } else {
                String link = result.getTargetRole().equals(com.web.utils.Contains.ROLE_TEACHER) ? "/teacher/blog-detail?id=" : "/student/blog-detail?id=";
                notificationService.saveToRole(result.getTargetRole(), "Bài viết mới", link+result.getId(), "Có bài viết mới: "+result.getTitle());
            }
        }
        return blogMapper.blogToResponse(result);
    }

    public void delete(Long id) {
        Optional<Blog> blog = blogRepository.findById(id);
        if (blog.isEmpty()){
            throw new MessageException("Blog not found");
        }
        blogRepository.delete(blog.get());
    }

    public Page<BlogResponse> findAll(Pageable pageable,String search,Long category) {
        Page<Blog> list;
        User user = userUtils.getUserWithAuthority();
        List<String> roles = new java.util.ArrayList<>();
        roles.add("ALL");
        boolean isAdmin = false;
        if (user != null) {
            roles.add(user.getAuthorities().getName());
            if (user.getAuthorities().getName().equals(com.web.utils.Contains.ROLE_ADMIN)) {
                isAdmin = true;
            }
        }

        if(category != null){
            if (isAdmin) {
                list = blogRepository.findByCategoryId(category, pageable);
            } else {
                list = blogRepository.findByCategoryIdAndTargetRoleIn(category, roles, pageable);
            }
        }
        else if(search != null && !search.isEmpty()){
            if (isAdmin) {
                list = blogRepository.search(search, pageable);
            } else {
                list = blogRepository.searchAndTargetRoleIn(search, roles, pageable);
            }
        }
        else{
            if (isAdmin) {
                list = blogRepository.findAll(pageable);
            } else {
                list = blogRepository.findByTargetRoleIn(roles, pageable);
            }
        }

        Page<BlogResponse> result = list.map(blog-> blogMapper.blogToResponse(blog));
        return result;
    }

    public BlogResponse findById(Long id) {
        Optional<Blog> blog = blogRepository.findById(id);
        if (blog.isEmpty()){
            throw new MessageException("Blog not found");
        }
        blog.get().setNumView(blog.get().getNumView() + 1);
        blogRepository.save(blog.get());
        return blogMapper.blogToResponse(blog.get());
    }

    public List<BlogResponse> bestView() {
        List<Blog> blog = blogRepository.bestView();
        List<BlogResponse> result = blog.stream().map(b-> blogMapper.blogToResponse(b)).collect(Collectors.toList()); ;
        return result;
    }
}
