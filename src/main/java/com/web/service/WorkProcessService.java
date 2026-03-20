package com.web.service;

import com.web.dto.request.WorkProcessRequest;
import com.web.dto.response.WorkProcessResponse;
import com.web.entity.*;
import com.web.exception.MessageException;
import com.web.mapper.WorkProcessMapper;
import com.web.repository.SemesterTeacherRepository;
import com.web.repository.StudentRegisRepository;
import com.web.repository.WorkProcessRepository;
import com.web.repository.WorkProcessStudentRepository;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WorkProcessService {

    @Autowired
    private WorkProcessRepository workProcessRepository;

    @Autowired
    private WorkProcessStudentRepository workProcessStudentRepository;

    @Autowired
    private SemesterTeacherRepository semesterTeacherRepository;

    @Autowired
    private StudentRegisRepository studentRegisRepository;

    @Autowired
    private WorkProcessMapper workProcessMapper;

    @Autowired
    private UserUtils userUtils;


    public WorkProcess save(WorkProcessRequest request){

        SemesterTeacher semesterTeacher = semesterTeacherRepository.findById(request.getSemesterTeacherId())
                .orElseThrow(() -> new MessageException("Không tìm thấy giảng viên hướng dẫn"));

        WorkProcess workProcess;

        if(request.getId() == null){
            workProcess = new WorkProcess();
            workProcess.setCreatedDate(LocalDateTime.now());
        }
        else{
            workProcess = workProcessRepository.findById(request.getId())
                    .orElseThrow(() -> new MessageException("Không tìm thấy tiến độ công việc"));
        }

        workProcess.setTitle(request.getTitle());
        workProcess.setDescription(request.getDescription());
        workProcess.setDeadline(request.getDeadline());
        workProcess.setSemesterTeacher(semesterTeacher);

        return workProcessRepository.save(workProcess);
    }


    @Transactional
    public Map<String,String> delete(Long id){
        try{
            workProcessStudentRepository.deleteByWorkProcess(id);
            workProcessRepository.deleteById(id);
            return Map.of("message","Xóa tiến độ công việc thành công");
        }
        catch (Exception e){
            throw new MessageException("Không thể xóa tiến độ công việc");
        }
    }


    public WorkProcess findById(Long id){
        return workProcessRepository.findById(id)
                .orElseThrow(() -> new MessageException("Không tìm thấy tiến độ công việc"));
    }


    public Page<WorkProcess> findAll(String search, Long semesterTeacherId, Pageable pageable){

        if(semesterTeacherId == null){
            throw new MessageException("semesterTeacherId không được để trống");
        }

        if(search == null || search.trim().isEmpty()){
            return workProcessRepository.findBySemesterTeacherId(semesterTeacherId, pageable);
        }
        else{
            search = "%" + search + "%";
            return workProcessRepository.findByParamAndTeacher(search, semesterTeacherId, pageable);
        }
    }

    public Page<WorkProcessResponse> findAllByStudent(String search, Long semesterTeacherId, Pageable pageable){

        if(semesterTeacherId == null){
            throw new MessageException("semesterTeacherId không được để trống");
        }

        User user = userUtils.getUserWithAuthority();
        StudentRegis studentRegis = studentRegisRepository
                .findByStudentAndSemesterTeacher(user.getId(), semesterTeacherId)
                .orElseThrow(() -> new MessageException("Không tìm thấy đăng ký"));

        Page<WorkProcess> page;

        if(search == null || search.trim().isEmpty()){
            page = workProcessRepository.findBySemesterTeacherId(semesterTeacherId, pageable);
        } else {
            search = "%" + search + "%";
            page = workProcessRepository.findByParamAndTeacher(search, semesterTeacherId, pageable);
        }

        // Lấy danh sách đã nộp
        List<WorkProcessStudent> submittedList =
                workProcessStudentRepository.findByStudentRegisId(studentRegis.getId(), semesterTeacherId);

        // Convert sang Map để tối ưu O(1)
        Map<Long, WorkProcessStudent> submittedMap = submittedList.stream()
                .collect(Collectors.toMap(
                        w -> w.getWorkProcess().getId(),
                        w -> w
                ));

        // 🔥 Convert Page<Entity> → Page<Response>
        Page<WorkProcessResponse> responsePage = page.map(w -> {
            WorkProcessResponse res = workProcessMapper.entityToResponse(w);

            WorkProcessStudent std = submittedMap.get(w.getId());
            if(std != null){
                res.setIsSubmitted(true);
                res.setWorkProcessStudentResponse(workProcessMapper.entityToResponse(std));
            } else {
                res.setIsSubmitted(false);
            }

            return res;
        });

        return responsePage;
    }


}