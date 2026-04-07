package com.web.service;

import com.web.dto.request.SemesterTeacherAdminRequest;
import com.web.entity.SemesterTeacher;
import com.web.entity.SemesterType;
import com.web.entity.User;
import com.web.exception.MessageException;
import com.web.repository.SemesterTypeRepository;
import com.web.repository.SemesterTeacherRepository;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SemesterTeacherService {

    @Autowired
    private SemesterTeacherRepository semesterTeacherRepository;

    @Autowired
    private SemesterTypeRepository semesterTypeRepository;

    @Autowired
    private UserRepository userRepository;

    public List<SemesterTeacher> findBySesType(Long sesTypeId){
        return semesterTeacherRepository.findBySesType(sesTypeId);
    }

    public List<SemesterTeacher> searchAdmin(Long semesterId, Long semesterTypeId, String keyword) {
        String normalizedKeyword = keyword == null || keyword.trim().isEmpty() ? null : keyword.trim();
        return semesterTeacherRepository.searchAdmin(semesterId, semesterTypeId, normalizedKeyword);
    }

    public SemesterTeacher findById(Long id) {
        return semesterTeacherRepository.findById(id)
                .orElseThrow(() -> new MessageException("Không tìm thấy giảng viên hướng dẫn"));
    }

    public SemesterTeacher saveAdmin(SemesterTeacherAdminRequest request) {
        if (request.getSemesterTypeId() == null) {
            throw new MessageException("Vui lòng chọn loại thực tập");
        }
        if (request.getTeacherId() == null) {
            throw new MessageException("Vui lòng chọn giảng viên");
        }
        if (request.getMaxStudents() == null || request.getMaxStudents() <= 0) {
            throw new MessageException("Số lượng sinh viên tối đa phải lớn hơn 0");
        }
        if (request.getProjectName() == null || request.getProjectName().trim().isEmpty()) {
            throw new MessageException("Tên đề tài không được bỏ trống");
        }

        SemesterType semesterType = semesterTypeRepository.findById(request.getSemesterTypeId())
                .orElseThrow(() -> new MessageException("Không tìm thấy loại thực tập"));
        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new MessageException("Không tìm thấy giảng viên"));

        SemesterTeacher semesterTeacher;
        if (request.getId() == null) {
            if (semesterTeacherRepository.findByTeacherAndSemesterType(request.getTeacherId(), request.getSemesterTypeId()).isPresent()) {
                throw new MessageException("Giảng viên đã được phân công trong loại thực tập này");
            }
            semesterTeacher = new SemesterTeacher();
            semesterTeacher.setCurrentStudents(0);
        } else {
            semesterTeacher = semesterTeacherRepository.findById(request.getId())
                    .orElseThrow(() -> new MessageException("Không tìm thấy giảng viên hướng dẫn"));
            if (!semesterTeacher.getTeacher().getId().equals(request.getTeacherId())
                    || !semesterTeacher.getSemesterType().getId().equals(request.getSemesterTypeId())) {
                if (semesterTeacherRepository.findByTeacherAndSemesterType(request.getTeacherId(), request.getSemesterTypeId()).isPresent()) {
                    throw new MessageException("Giảng viên đã được phân công trong loại thực tập này");
                }
            }
        }

        if (semesterTeacher.getCurrentStudents() != null && request.getMaxStudents() < semesterTeacher.getCurrentStudents()) {
            throw new MessageException("Số lượng tối đa không được nhỏ hơn số sinh viên đã đăng ký");
        }

        semesterTeacher.setSemesterType(semesterType);
        semesterTeacher.setTeacher(teacher);
        semesterTeacher.setMaxStudents(request.getMaxStudents());
        semesterTeacher.setProjectName(request.getProjectName().trim());
        semesterTeacher.setDescriptionProject(request.getDescriptionProject());
        return semesterTeacherRepository.save(semesterTeacher);
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
