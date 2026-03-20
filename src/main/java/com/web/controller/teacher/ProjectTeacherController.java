package com.web.controller.teacher;

import com.web.entity.*;
import com.web.exception.MessageException;
import com.web.repository.*;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher")
public class ProjectTeacherController {

    @Autowired
    private SemesterTeacherRepository semesterTeacherRepository;

    @Autowired
    private StudentRegisRepository studentRegisRepository;

    @Autowired
    private WorkProcessRepository workProcessRepository;

    @Autowired
    private WorkProcessStudentRepository workProcessStudentRepository;

    @Autowired
    private RelatedDocumentsRepository relatedDocumentsRepository;

    @Autowired
    private RelatedDocumentStudentRepository relatedDocumentStudentRepository;

    @Autowired
    private UserUtils userUtils;

    @RequestMapping(value = {"/project"}, method = RequestMethod.GET)
    public String project(Model model) {
        User user = userUtils.getUserWithAuthority();
        List<SemesterTeacher> semesterTeachers = semesterTeacherRepository.findByTeacher(user.getId());
        model.addAttribute("semesterTeachers",semesterTeachers);
        return "teacher/project/project";
    }

    @RequestMapping(value = {"/project-detail/{id}"}, method = RequestMethod.GET)
    public String projectDetail(Model model, @PathVariable("id") Long id) {
        SemesterTeacher semesterTeacher = semesterTeacherRepository.findById(id).orElseThrow(()->new MessageException("Không tìm thấy đề tài thực tập"));
        List<StudentRegis> studentRegis = studentRegisRepository.findBySemesterTeacher(id);
        List<WorkProcess> workProcesses = workProcessRepository.findBySemesterTeacherId(id);
        model.addAttribute("semesterTeacher",semesterTeacher);
        model.addAttribute("studentRegis",studentRegis);
        model.addAttribute("workProcesses",workProcesses);
        model.addAttribute("countRelate",relatedDocumentsRepository.countBySemesterTeacher(id));
        model.addAttribute("semesterTeacherId",id);
        return "teacher/project/project-detail";
    }


    @RequestMapping(value = {"/work-student/{workProcessId}"}, method = RequestMethod.GET)
    public String workStudent(Model model, @PathVariable("workProcessId") Long id) {
        WorkProcess workProcess = workProcessRepository.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy đề tài thực tập"));
        SemesterTeacher semesterTeacher = workProcess.getSemesterTeacher();
        model.addAttribute("semesterTeacher",semesterTeacher);
        List<StudentRegis> studentRegisList = studentRegisRepository.findBySemesterTeacher(semesterTeacher.getId());

        List<WorkProcessStudent> workProcessStudents = workProcessStudentRepository.findByWorkProcess(id);

        Set<StudentRegis> submittedSet = workProcessStudents.stream()
                .map(WorkProcessStudent::getStudentRegis)
                .collect(Collectors.toSet());

        List<StudentRegis> notSubmitted = studentRegisList.stream()
                .filter(s -> !submittedSet.contains(s))
                .collect(Collectors.toList());

        Map<Long, WorkProcessStudent> workMap = workProcessStudents.stream()
                .collect(Collectors.toMap(
                        wp -> wp.getStudentRegis().getId(),
                        wp -> wp,
                        (a, b) -> a.getCreatedDate().isAfter(b.getCreatedDate()) ? a : b
                ));

        model.addAttribute("workProcess", workProcess);
        model.addAttribute("workMap", workMap);
        model.addAttribute("submittedSet",submittedSet);
        model.addAttribute("notSubmitted",notSubmitted);
        model.addAttribute("workProcessStudents",workProcessStudents);
        model.addAttribute("semesterTeacherId",semesterTeacher.getId());

        return "teacher/project/work-student";
    }

    @RequestMapping(value = {"/document-student/{relatedDocumentId}"}, method = RequestMethod.GET)
    public String relatedDocument(Model model, @PathVariable("relatedDocumentId") Long id) {
        RelatedDocuments relatedDocuments = relatedDocumentsRepository.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy dữ liệu"));
        SemesterTeacher semesterTeacher = relatedDocuments.getSemesterTeacher();
        model.addAttribute("semesterTeacher",semesterTeacher);

        List<StudentRegis> studentRegisList = studentRegisRepository.findBySemesterTeacher(semesterTeacher.getId());

        List<RelatedDocumentStudent> relatedDocumentStudents = relatedDocumentStudentRepository.findByRelatedDocumentId(id);

        Set<StudentRegis> submittedSet = relatedDocumentStudents.stream()
                .map(RelatedDocumentStudent::getStudentRegis)
                .collect(Collectors.toSet());

        List<StudentRegis> notSubmitted = studentRegisList.stream()
                .filter(s -> !submittedSet.contains(s))
                .collect(Collectors.toList());

        Map<Long, RelatedDocumentStudent> workMap = relatedDocumentStudents.stream()
                .collect(Collectors.toMap(
                        wp -> wp.getStudentRegis().getId(),
                        wp -> wp,
                        (a, b) -> a.getCreatedDate().isAfter(b.getCreatedDate()) ? a : b
                ));

        model.addAttribute("relatedDocuments", relatedDocuments);
        model.addAttribute("workMap", workMap);
        model.addAttribute("submittedSet",submittedSet);
        model.addAttribute("notSubmitted",notSubmitted);
        model.addAttribute("relatedDocumentStudents",relatedDocumentStudents);
        model.addAttribute("semesterTeacherId",semesterTeacher.getId());
        model.addAttribute("studentRegisList",studentRegisList);

        return "teacher/project/document-student";
    }

    @GetMapping("/student-dashboard/{studentRegisId}")
    public String studentDashboard(Model model,
                                   @PathVariable("studentRegisId") Long studentRegisId) {

        // 1. Lấy sinh viên
        StudentRegis studentRegis = studentRegisRepository.findById(studentRegisId)
                .orElseThrow(() -> new MessageException("Không tìm thấy sinh viên"));

        model.addAttribute("studentRegis", studentRegis);

        // 2. Lấy danh sách tiến độ đã nộp
        List<WorkProcessStudent> workList =
                workProcessStudentRepository.findByStudentRegisId(studentRegisId);

        // 3. Lấy tất cả tiến độ (để tính chưa nộp)
        List<WorkProcess> allWork =
                workProcessRepository.findBySemesterTeacherId(studentRegis.getSemesterTeacher().getId());

        // 4. Lấy danh sách tài liệu đã nộp
        List<RelatedDocumentStudent> docList =
                relatedDocumentStudentRepository.findByStudentRegisId(studentRegisId);

        // 5. Lấy tất cả tài liệu
        List<RelatedDocuments> allDocs =
                relatedDocumentsRepository.findBySemesterTeacher(studentRegis.getSemesterTeacher().getId());

        // ======================
        // 📊 THỐNG KÊ
        // ======================

        int onTime = 0;
        int late = 0;

        for (WorkProcessStudent wp : workList) {
            if (wp.getWorkProcess().getDeadline() != null) {
                if (wp.getCreatedDate().isAfter(wp.getWorkProcess().getDeadline())) {
                    late++;
                } else {
                    onTime++;
                }
            }
        }

        int notSubmitWork = allWork.size() - workList.size();
        int notSubmitDoc = allDocs.size() - docList.size();

        // ======================
        // 📦 ĐƯA VÀO MODEL
        // ======================

        model.addAttribute("workList", workList);
        model.addAttribute("docList", docList);

        model.addAttribute("onTimeCount", onTime);
        model.addAttribute("lateCount", late);
        model.addAttribute("notSubmitWork", notSubmitWork);
        model.addAttribute("notSubmitDoc", notSubmitDoc);

        model.addAttribute("totalScore", studentRegis.getTotalScore());
        model.addAttribute("studentRegis", studentRegis);
        model.addAttribute("semesterTeacherId",studentRegis.getSemesterTeacher().getId());
        model.addAttribute("semesterTeacher",studentRegis.getSemesterTeacher());

        return "teacher/project/student-dashboard";
    }
}
