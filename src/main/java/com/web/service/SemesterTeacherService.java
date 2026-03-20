package com.web.service;

import com.web.entity.SemesterTeacher;
import com.web.entity.User;
import com.web.exception.MessageException;
import com.web.repository.SemesterTeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SemesterTeacherService {

    @Autowired
    private SemesterTeacherRepository semesterTeacherRepository;

    public List<SemesterTeacher> findBySesType(Long sesTypeId){
        return semesterTeacherRepository.findBySesType(sesTypeId);
    }

    public void delete(Long id) {
        try {
            semesterTeacherRepository.deleteById(id);
        }
        catch (Exception e){
            throw new MessageException("Giảng viên trong năm học này đã có liên kết, không thể xóa");
        }
    }
}
