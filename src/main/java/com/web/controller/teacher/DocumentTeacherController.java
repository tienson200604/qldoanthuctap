package com.web.controller.teacher;

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
@RequestMapping("/teacher")
public class DocumentTeacherController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @GetMapping("/documents")
    public String documents(Model model) {
        model.addAttribute("categories", categoryRepository.findByCategoryType(CategoryType.DOCUMENT));
        return "teacher/document";
    }

    @GetMapping("/my-document")
    public String myDocument(Model model) {
        model.addAttribute("categories", categoryRepository.findByCategoryType(CategoryType.DOCUMENT));
        return "teacher/my-document";
    }

    @GetMapping("/upload-document")
    public String uploadDocument(Model model) {
        return "teacher/upload-document";
    }

    @GetMapping("/document-detail")
    public String documentDetail(Model model, @RequestParam Long id) {
        Document document = documentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy tài liệu nào"));
        document.setNumberView(document.getNumberView() + 1);
        documentRepository.save(document);
        model.addAttribute("detail", document);
        model.addAttribute("documentLq", documentRepository.documentLq(document.getCategory().getId(), id));
        return "teacher/document-detail";
    }
}
