package com.web.controller.publicc;

import com.web.entity.User;
import com.web.utils.Contains;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PublicController {

    @Autowired
    private UserUtils userUtils;

    @RequestMapping(value = {"/","/login"}, method = RequestMethod.GET)
    public String login() {
        User user = userUtils.getUserWithAuthority();
        if(user != null){
            if(user.getAuthorities().getName().equals(Contains.ROLE_ADMIN)){
                return "redirect:/admin/index";
            }
            if(user.getAuthorities().getName().equals(Contains.ROLE_STUDENT)){
                return "redirect:/student/index";
            }
            if(user.getAuthorities().getName().equals(Contains.ROLE_TEACHER)){
                return "redirect:/teacher/index";
            }
        }
        return "public/login";
    }

    @RequestMapping(value = {"/forgot"}, method = RequestMethod.GET)
    public String forgot() {
        return "public/forgot";
    }

    @RequestMapping(value = {"/datlaimatkhau"}, method = RequestMethod.GET)
    public String datlaimatkhau() {
        return "public/datlaimatkhau";
    }
}
