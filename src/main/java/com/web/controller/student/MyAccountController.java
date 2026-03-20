package com.web.controller.student;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/student")
public class MyAccountController {

    @RequestMapping(value = {"/my-account"}, method = RequestMethod.GET)
    public String home(Model model) {
        return "student/my-account";
    }

    @RequestMapping(value = {"/change-password"}, method = RequestMethod.GET)
    public String changePass(Model model) {
        return "student/change-password";
    }
}
