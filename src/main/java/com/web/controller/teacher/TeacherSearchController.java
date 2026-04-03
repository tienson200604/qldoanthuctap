package com.web.controller.teacher;

import com.web.repository.BlogRepository;
import com.web.repository.CompanyRepository;
import com.web.repository.DocumentRepository;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/teacher")
public class TeacherSearchController {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/search")
    public String search(@RequestParam String search, Model model) {
        model.addAttribute("searchParam", search);
        String searchPr = "%" + search + "%";
        model.addAttribute("documents", documentRepository.search(searchPr));
        model.addAttribute("blogs", blogRepository.search(searchPr));
        model.addAttribute("companies", companyRepository.findByParam(searchPr));
        // Search students
        model.addAttribute("students", userRepository.getUserByRole(searchPr, "ROLE_STUDENT", PageRequest.of(0, 50)).getContent());
        return "teacher/search";
    }
}
