package com.web.controller.student;

import com.web.enums.CategoryType;
import com.web.repository.BlogRepository;
import com.web.repository.CompanyRepository;
import com.web.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/student")
public class SearchController {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/search")
    public String documents(@RequestParam String search, Model model) {
        model.addAttribute("searchParam", search);
        String searchPr = "%"+search+"%";
        model.addAttribute("documents", documentRepository.search(searchPr));
        model.addAttribute("blogs", blogRepository.search(searchPr));
        model.addAttribute("companies", companyRepository.findByParam(searchPr));
        return "student/search";
    }
}
