package com.web.controller.teacher;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/teacher")
public class MyAccountTeacherController {

    @RequestMapping(value = {"/my-account"}, method = RequestMethod.GET)
    public String home(Model model) {
        return "teacher/my-account";
    }

    @RequestMapping(value = {"/change-password"}, method = RequestMethod.GET)
    public String changePass(Model model) {
        return "teacher/change-password";
    }
}
