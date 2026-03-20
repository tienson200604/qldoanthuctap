package com.web.service;
import com.web.dto.response.RelatedDocumentsResponse;
import com.web.entity.*;
import com.web.exception.MessageException;
import com.web.mapper.RelatedDocumentMapper;
import com.web.repository.RelatedDocumentStudentRepository;
import com.web.repository.RelatedDocumentsRepository;
import com.web.repository.SemesterTeacherRepository;
import com.web.repository.StudentRegisRepository;
import com.web.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatedDocumentsService {

    @Autowired
    private RelatedDocumentsRepository relatedDocumentsRepository;

    @Autowired
    private RelatedDocumentStudentRepository relatedDocumentStudentRepository;

    @Autowired
    private RelatedDocumentMapper relatedDocumentMapper;

    @Autowired
    private StudentRegisRepository studentRegisRepository;

    @Autowired
    private UserUtils userUtils;

    public RelatedDocuments save(RelatedDocuments relatedDocuments){
        if(relatedDocuments.getId() == null){
            if(relatedDocumentsRepository.findByNameAndSemesterTeacher(relatedDocuments.getName(), relatedDocuments.getSemesterTeacher().getId()).isPresent()){
                throw new MessageException("Loại giấy tờ này đã được tạo");
            }
        }
        else{
            if(relatedDocumentsRepository.findByNameAndSemesterTeacher(relatedDocuments.getName(), relatedDocuments.getSemesterTeacher().getId(), relatedDocuments.getId()).isPresent()){
                throw new MessageException("Loại giấy tờ này đã được tạo");
            }
            RelatedDocuments ex = relatedDocumentsRepository.findById(relatedDocuments.getId()).get();
            relatedDocuments.setOnTimeCount(ex.getOnTimeCount());
            relatedDocuments.setOutTimeCount(ex.getOutTimeCount());
        }
        relatedDocumentsRepository.save(relatedDocuments);
        return relatedDocuments;
    }

    public List<RelatedDocuments> findBySemesterTeacher(Long semesterTeacherId){
        return relatedDocumentsRepository.findBySemesterTeacher(semesterTeacherId);
    }

    public List<RelatedDocumentsResponse> findBySemesterTeacherForStudent(Long semesterTeacherId){

        if(semesterTeacherId == null){
            throw new MessageException("semesterTeacherId không được để trống");
        }

        User user = userUtils.getUserWithAuthority();

        StudentRegis studentRegis = studentRegisRepository
                .findByStudentAndSemesterTeacher(user.getId(), semesterTeacherId)
                .orElseThrow(() -> new MessageException("Không tìm thấy đăng ký"));

        // Lấy danh sách tài liệu
        List<RelatedDocuments> list = relatedDocumentsRepository
                .findBySemesterTeacher(semesterTeacherId);

        // Lấy danh sách đã nộp
        List<RelatedDocumentStudent> submitted =
                relatedDocumentStudentRepository
                        .findByStudentRegisAndSemesterTeacherId(studentRegis.getId(), semesterTeacherId);

        // 🔥 Convert sang Map
        Map<Long, RelatedDocumentStudent> submittedMap = submitted.stream()
                .collect(Collectors.toMap(
                        s -> s.getRelatedDocuments().getId(),
                        s -> s
                ));

        // 🔥 Map sang response + set trạng thái luôn
        List<RelatedDocumentsResponse> result = list.stream().map(r -> {
            RelatedDocumentsResponse res = relatedDocumentMapper.entityToResponse(r);

            RelatedDocumentStudent std = submittedMap.get(r.getId());

            if(std != null){
                res.setIsSubmitted(true);
                res.setRelatedDocumentStudentResponse(
                        relatedDocumentMapper.entityToResponse(std)
                );
            } else {
                res.setIsSubmitted(false);
            }

            return res;
        }).collect(Collectors.toList());

        return result;
    }
    @Transactional
    public void delete(Long id){
        relatedDocumentStudentRepository.deleteByRelatedDocuments(id);
        relatedDocumentsRepository.deleteById(id);
    }

    public RelatedDocuments findById(Long id){
        return relatedDocumentsRepository.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy dữ liệu"));
    }
}
