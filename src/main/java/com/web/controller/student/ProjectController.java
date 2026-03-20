package com.web.controller.student;

import com.web.entity.*;
import com.web.exception.MessageException;
import com.web.repository.*;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
public class ProjectController {

    @Autowired
    private StudentRegisRepository studentRegisRepository;

    @Autowired
    private WorkProcessStudentRepository workProcessStudentRepository;

    @Autowired
    private SemesterTeacherRepository semesterTeacherRepository;

    @Autowired
    private WorkProcessRepository workProcessRepository;

    @Autowired
    private RelatedDocumentsRepository relatedDocumentsRepository;

    @Autowired
    private RateRepository rateRepository;

    @Autowired
    private UserUtils userUtils;

    @RequestMapping(value = {"/project"}, method = RequestMethod.GET)
    public String project(Model model) {
        User user = userUtils.getUserWithAuthority();
        List<StudentRegis> studentRegis = studentRegisRepository.findByUser(user.getId());
        model.addAttribute("studentRegis",studentRegis);
        return "student/project/project";
    }

    @RequestMapping(value = {"/project-detail/{studentRegisId}"}, method = RequestMethod.GET)
    public String projectDetail(@PathVariable("studentRegisId") Long studentRegisId, Model model) {
        StudentRegis studentRegis = studentRegisRepository.findById(studentRegisId).orElseThrow(() -> new MessageException("Không tìm thấy đăng ký"));
        SemesterTeacher semesterTeacher = studentRegis.getSemesterTeacher();
        model.addAttribute("semesterTeacher",semesterTeacher);
        List<StudentRegis> listStudentRegis = studentRegisRepository.findBySemesterTeacher(semesterTeacher.getId());

        List<WorkProcess> workProcesses = workProcessRepository.findBySemesterTeacherId(semesterTeacher.getId());


        model.addAttribute("semesterTeacher",semesterTeacher);
        model.addAttribute("studentRegis",listStudentRegis);
        model.addAttribute("workProcesses",workProcesses);
        model.addAttribute("countRelate",relatedDocumentsRepository.countBySemesterTeacher(semesterTeacher.getId()));
        model.addAttribute("semesterTeacherId",semesterTeacher.getId());
        model.addAttribute("studentRegisId",studentRegisId);
        Optional<Rate> rate = rateRepository.findByStudentRegisId(studentRegisId);
        if(rate.isPresent()){
            model.addAttribute("rate",rate.get());
        }
        else{
            model.addAttribute("rate",new Rate());
        }
        return "student/project/project-detail";
    }

}
