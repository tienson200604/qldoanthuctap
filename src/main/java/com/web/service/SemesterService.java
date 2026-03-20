package com.web.service;

import com.web.entity.Category;
import com.web.entity.Semester;
import com.web.exception.MessageException;
import com.web.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SemesterService {

    @Autowired
    private SemesterRepository repo;

    public Semester save(Semester semester) {
        if (semester.getId() == null && repo.existsByYearName(semester.getYearName())) {
            throw new MessageException("Năm học này đã tồn tại");
        }
        if (semester.getId() != null && repo.existsByYearName(semester.getYearName(), semester.getId())) {
            throw new MessageException("Năm học này đã tồn tại");
        }

        // Nếu set năm học này là active, cần tắt các năm học khác
        if (semester.getIsActive()) {
            repo.findByIsActiveTrue().ifPresent(s -> {
                s.setIsActive(false);
                repo.save(s);
            });
        }
        return repo.save(semester);
    }

    public List<Semester> getAll() {
        return repo.findAll();
    }

    public Semester findById(Long id){
        return repo.findById(id).orElseThrow(() -> new MessageException("Không tồn tại năm học với id: "+id));
    }


    public Map<String, String> delete(Long id) {
        try {
            repo.deleteById(id);
            return Map.of("message", "Xóa năm học thành công");
        }
        catch (Exception e){
            throw new MessageException("Năm học đã có dữ liệu liên kết, không thể xóa");
        }
    }
}
