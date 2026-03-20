package com.web.controller.teacher;

import com.web.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teacher")
public class CompanyTeacherController {

    @Autowired
    private SemesterRepository semesterRepository;

    @GetMapping("/company")
    public String blogs(Model model) {
        model.addAttribute("semesters", semesterRepository.findAll());
        return "teacher/company";
    }
}
