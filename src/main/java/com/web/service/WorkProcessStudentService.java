package com.web.service;

import com.web.dto.request.WorkProcessStudentRequest;
import com.web.entity.StudentRegis;
import com.web.entity.User;
import com.web.entity.WorkProcess;
import com.web.entity.WorkProcessStudent;
import com.web.exception.MessageException;
import com.web.repository.StudentRegisRepository;
import com.web.repository.WorkProcessRepository;
import com.web.repository.WorkProcessStudentRepository;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WorkProcessStudentService {

    @Autowired
    private WorkProcessStudentRepository workProcessStudentRepository;

    @Autowired
    private StudentRegisRepository studentRegisRepository;

    @Autowired
    private WorkProcessRepository workProcessRepository;

    @Autowired
    private UserUtils userUtils;


    @Transient
    public WorkProcessStudent save(WorkProcessStudentRequest request){

        User user = userUtils.getUserWithAuthority();

        WorkProcess workProcess = workProcessRepository.findById(request.getWorkProcessId())
                .orElseThrow(() -> new MessageException("Không tìm thấy tiến độ"));

        Optional<StudentRegis> studentRegis = studentRegisRepository.findByStudentAndSemesterTeacher(user.getId(), workProcess.getSemesterTeacher().getId());


        WorkProcessStudent workProcessStudent=null;
        Optional<WorkProcessStudent> optional = workProcessStudentRepository.findByStudentAndWorkProcess(studentRegis.get().getId(), workProcess.getId());
        if(optional.isPresent()){
            workProcessStudent = optional.get();
        }
        else{
            workProcessStudent = new WorkProcessStudent();
            if(LocalDateTime.now().isBefore(workProcess.getDeadline())){
                workProcess.setOnTimeCount(workProcess.getOnTimeCount() + 1);
            }
            else{
                workProcess.setOutTimeCount(workProcess.getOutTimeCount() + 1);
            }
        }


        workProcessStudent.setTitle(request.getTitle());
        workProcessStudent.setContent(request.getContent());
        workProcessStudent.setFile(request.getFile());
        workProcessStudent.setPercent(request.getPercent());
        workProcessStudent.setStudentRegis(studentRegis.get());
        workProcessStudent.setWorkProcess(workProcess);
        workProcessRepository.save(workProcess);
        return workProcessStudentRepository.save(workProcessStudent);
    }


    public Map<String,String> delete(Long id){
        try{
            workProcessStudentRepository.deleteById(id);
            return Map.of("message","Xóa thành công");
        }
        catch(Exception e){
            throw new MessageException("Không thể xóa");
        }
    }


    public WorkProcessStudent findById(Long id){
        return workProcessStudentRepository.findById(id)
                .orElseThrow(() -> new MessageException("Không tìm thấy tiến độ công việc"));
    }


    public Page<WorkProcessStudent> findAll(String search, Long workProcessId, Pageable pageable){

        if(workProcessId == null){
            throw new MessageException("workProcessId không được để trống");
        }

        if(search == null || search.trim().isEmpty()){
            return workProcessStudentRepository.findByWorkProcessId(workProcessId, pageable);
        }
        else{
            search = "%" + search + "%";
            return workProcessStudentRepository.findByParamAndWorkProcess(search, workProcessId, pageable);
        }
    }


    public Map<String, Object> findByWorkProcess(Long workProcessId) {
        WorkProcess workProcess = workProcessRepository.findById(workProcessId).get();
        User user = userUtils.getUserWithAuthority();
        Map<String, Object> map = new HashMap<>();
        StudentRegis studentRegis = studentRegisRepository.findByStudentAndSemesterTeacher(user.getId(), workProcess.getSemesterTeacher().getId()).get();
        Optional<WorkProcessStudent> workProcessStudent = workProcessStudentRepository.findByStudentAndWorkProcess(studentRegis.getId(), workProcessId);
        if(workProcessStudent.isPresent()){
            map.put("data",workProcessStudent.get());
        }
        else{
            map.put("data",null);
        }
        return map;
    }

    public WorkProcessStudent replay(Long id, String replay) {
        WorkProcessStudent workProcessStudent = workProcessStudentRepository.findById(id).orElseThrow(() -> new MessageException("KhÔng tìm thấy dữ liệu"));
        workProcessStudent.setReplay(replay);
        workProcessStudent.setCreatedDate(LocalDateTime.now());
        return workProcessStudentRepository.save(workProcessStudent);
    }
}