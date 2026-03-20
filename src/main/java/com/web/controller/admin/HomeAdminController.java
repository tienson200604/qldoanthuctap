package com.web.controller.admin;

import com.web.repository.DashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class HomeAdminController {

    @Autowired
    private DashboardRepository dashboardRepository;

    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("totalStudent", dashboardRepository.countStudent());
        model.addAttribute("totalRegis", dashboardRepository.countStudentRegis());
        model.addAttribute("totalCompany", dashboardRepository.countCompany());
        model.addAttribute("totalProject", dashboardRepository.countProject());
        model.addAttribute("totalDocument", dashboardRepository.countDocument());
        model.addAttribute("totalBlog", dashboardRepository.countBlog());
        model.addAttribute("topStudent", dashboardRepository.topStudent());
        return "admin/index";
    }
}
