package com.web.service;

import com.web.dto.request.SemesterTypeRequest;
import com.web.dto.response.SemesterTypeResponse;
import com.web.entity.*;
import com.web.enums.InternshipType;
import com.web.exception.MessageException;
import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class SemesterTypeService {

    @Autowired
    private SemesterTypeRepository semesterTypeRepository;

    @Autowired
    private SemesterTeacherRepository semesterTeacherRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private SemesterCompanyRepository semesterCompanyRepository;

    @Autowired
    private UserRepository userRepository;


    @Transactional
    public SemesterType save(SemesterTypeRequest request){

        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new MessageException("Không tìm thấy học kỳ"));

        SemesterType semesterType;

        if(request.getId() == null){
            semesterType = new SemesterType();
        }
        else{
            semesterType = semesterTypeRepository.findById(request.getId())
                    .orElseThrow(() -> new MessageException("Không tìm thấy loại đề tài"));
        }

        semesterType.setType(request.getType());
        semesterType.setSemester(semester);
        semesterType.setDeadlineRegis(request.getDeadlineRegis());

        semesterTypeRepository.save(semesterType);


        // xử lý teacher
        for(SemesterTypeRequest.SemesterTeacherRequest t : request.getSemesterTeacherRequests()){

            User teacher = userRepository.findById(t.getTeacherId())
                    .orElseThrow(() -> new MessageException("Không tìm thấy giảng viên"));

            SemesterTeacher semesterTeacher;

            if(t.getId() == null){
                if(semesterTeacherRepository.findByTeacherAndSemesterType(t.getTeacherId(), semesterType.getId()).isPresent()){
                    continue;
                }
                semesterTeacher = new SemesterTeacher();
                semesterTeacher.setCurrentStudents(0);
                semesterTeacher.setSemesterType(semesterType);
            }
            else{
                semesterTeacher = semesterTeacherRepository.findById(t.getId())
                        .orElseThrow(() -> new MessageException("Không tìm thấy giảng viên hướng dẫn"));
            }

            semesterTeacher.setMaxStudents(t.getMaxStudents());
            semesterTeacher.setTeacher(teacher);
            semesterTeacher.setProjectName(t.getProjectName());
            semesterTeacher.setDescriptionProject(t.getDescriptionProject());
            semesterTeacherRepository.save(semesterTeacher);
        }

        return semesterType;
    }


    public Map<String,String> delete(Long id){
        try{
            semesterTypeRepository.deleteById(id);
            return Map.of("message","Xóa loại đề tài thành công");
        }
        catch (Exception e){
            throw new MessageException("Không thể xóa loại đề tài");
        }
    }


    public SemesterType findById(Long id){
        return semesterTypeRepository.findById(id)
                .orElseThrow(() -> new MessageException("Không tìm thấy loại đề tài"));
    }


    public List<SemesterType> findAll(Long semesterId){
        return semesterTypeRepository.findByParamAndSemester(semesterId);
    }

    public SemesterTypeResponse findByType(InternshipType type) {
        SemesterTypeResponse response = new SemesterTypeResponse();
        SemesterType semesterType = semesterTypeRepository.findByType(type);
        if(semesterType == null){
            throw new MessageException("Xin lỗi, loại thực tập này chưa được mở");
        }
        List<SemesterTeacher> semesterTeachers = semesterTeacherRepository.findBySesType(semesterType.getId());
        response.setSemester(semesterType.getSemester());
        response.setType(type);
        response.setId(semesterType.getId());
        response.setSemesterTeachers(semesterTeachers);

        List<SemesterCompany> semesterCompanies = semesterCompanyRepository.findBySemesterId(semesterType.getSemester().getId());
        response.setSemesterCompanies(semesterCompanies);

        return response;
    }
}