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

    @Autowired
    private com.web.utils.UserUtils userUtils;

    @RequestMapping(value = {"/index"}, method = RequestMethod.GET)
    public String home(Model model) {
        java.util.List<String> roles = new java.util.ArrayList<>();
        roles.add("ALL");
        com.web.entity.User user = userUtils.getUserWithAuthority();
        if (user != null) {
            roles.add(user.getAuthorities().getName());
        }
        model.addAttribute("newBlogs", blogRepository.newBlog(roles, org.springframework.data.domain.PageRequest.of(0, 10)));
        return "teacher/index";
    }


}
