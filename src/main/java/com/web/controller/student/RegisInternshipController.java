package com.web.controller.student;

import com.web.entity.SemesterCompany;
import com.web.entity.SemesterTeacher;
import com.web.entity.SemesterType;
import com.web.enums.CategoryType;
import com.web.enums.InternshipType;
import com.web.repository.SemesterCompanyRepository;
import com.web.repository.SemesterTeacherRepository;
import com.web.repository.SemesterTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/student")
public class RegisInternshipController {

    @Autowired
    private SemesterCompanyRepository semesterCompanyRepository;

    @Autowired
    private SemesterTeacherRepository semesterTeacherRepository;

    @Autowired
    private SemesterTypeRepository semesterTypeRepository;

    @GetMapping("/regis-internship")
    public String documents(Model model) {

        List<SemesterCompany> semesterCompanies = semesterCompanyRepository.findBySemesterActive();

        List<SemesterTeacher> doanhNghiepNgoai = semesterTeacherRepository.findByTypeAndSemesterActive(InternshipType.DOANH_NGHIEP_NGOAI);
        List<SemesterTeacher> doanhNghiepLienKet = semesterTeacherRepository.findByTypeAndSemesterActive(InternshipType.DOANH_NGHIEP_LIEN_KET);
        List<SemesterTeacher> taiTruong = semesterTeacherRepository.findByTypeAndSemesterActive(InternshipType.TAI_TRUONG);

        SemesterType checkDnNgoai = semesterTypeRepository.findByType(InternshipType.DOANH_NGHIEP_NGOAI);
        SemesterType checkDnLienKet = semesterTypeRepository.findByType(InternshipType.DOANH_NGHIEP_LIEN_KET);
        SemesterType checkTaiTruong = semesterTypeRepository.findByType(InternshipType.TAI_TRUONG);

        model.addAttribute("semesterCompanies",semesterCompanies);

        model.addAttribute("doanhNghiepNgoai",doanhNghiepNgoai);
        model.addAttribute("doanhNghiepLienKet",doanhNghiepLienKet);
        model.addAttribute("taiTruong",taiTruong);

        model.addAttribute("checkDnNgoai",checkDnNgoai);
        model.addAttribute("checkDnLienKet",checkDnLienKet);
        model.addAttribute("checkTaiTruong",checkTaiTruong);

        return "student/regis-internship.html";
    }
}
