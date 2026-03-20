package com.web.controller.student;

import com.web.entity.Semester;
import com.web.enums.CategoryType;
import com.web.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/student")
public class CompanyUserController {

    @Autowired
    private SemesterRepository semesterRepository;

    @GetMapping("/company")
    public String blogs(Model model) {
        model.addAttribute("semesters", semesterRepository.findAll());
        return "student/company";
    }
}
