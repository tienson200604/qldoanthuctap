package com.web.controller.student;

import com.web.entity.Blog;
import com.web.entity.Document;
import com.web.enums.CategoryType;
import com.web.repository.CategoryRepository;
import com.web.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/student")
public class DocumentUserController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @GetMapping("/documents")
    public String documents(Model model) {
        model.addAttribute("categories", categoryRepository.findByCategoryType(CategoryType.DOCUMENT));
        return "student/document";
    }

    @GetMapping("/document-detail")
    public String documentDetail(Model model, @RequestParam Long id) {
        Document document = documentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy tài liệu nào"));
        document.setNumberView(document.getNumberView() + 1);
        documentRepository.save(document);
        model.addAttribute("detail", document);
        model.addAttribute("documentLq", documentRepository.documentLq(document.getCategory().getId(), id));
        return "student/document-detail";
    }
}
