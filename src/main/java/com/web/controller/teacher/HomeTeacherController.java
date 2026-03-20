package com.web.controller.teacher;

import com.web.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/teacher")
public class HomeTeacherController {

    @Autowired
    private BlogRepository blogRepository;

    @RequestMapping(value = {"/index"}, method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("newBlogs", blogRepository.newBlog());
        return "teacher/index";
    }


}
