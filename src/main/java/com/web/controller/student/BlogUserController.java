package com.web.controller.student;

import com.google.api.gax.rpc.NotFoundException;
import com.web.entity.Blog;
import com.web.enums.CategoryType;
import com.web.exception.MessageException;
import com.web.repository.BlogRepository;
import com.web.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/student")
public class BlogUserController {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @RequestMapping(value = {"/blog-detail"}, method = RequestMethod.GET)
    public String blogDetail(Model model, @RequestParam Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy thông báo nào"));
        model.addAttribute("detail", blog);
        model.addAttribute("listBlogNew", blogRepository.newBlogAndNotId(id));
        model.addAttribute("listCategory", categoryRepository.findByCategoryType(CategoryType.BLOG));
        return "student/blog-detail";
    }

    @GetMapping("/blogs")
    public String blogs(Model model, @RequestParam(required = false) Long category) {
        model.addAttribute("categories", categoryRepository.findByCategoryType(CategoryType.BLOG));
        model.addAttribute("category", category);
        return "student/blogs";
    }
}
