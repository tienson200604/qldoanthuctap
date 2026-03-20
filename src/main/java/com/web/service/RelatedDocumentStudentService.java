package com.web.service;

import com.web.dto.request.WorkProcessStudentRequest;
import com.web.entity.*;
import com.web.exception.MessageException;
import com.web.repository.RelatedDocumentStudentRepository;
import com.web.repository.RelatedDocumentsRepository;
import com.web.repository.StudentRegisRepository;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RelatedDocumentStudentService {

    @Autowired
    private RelatedDocumentStudentRepository relatedDocumentStudentRepository;

    @Autowired
    private RelatedDocumentsRepository relatedDocumentsRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private StudentRegisRepository studentRegisRepository;

    @Transient
    public RelatedDocumentStudent save(RelatedDocumentStudent request){

        User user = userUtils.getUserWithAuthority();
        RelatedDocuments relatedDocuments = relatedDocumentsRepository.findById(request.getRelatedDocuments().getId()).orElseThrow(() -> new MessageException("Không tìm thấy dữ liệu"));
        Optional<StudentRegis> studentRegis = studentRegisRepository.findByStudentAndSemesterTeacher(user.getId(), relatedDocuments.getSemesterTeacher().getId());

        RelatedDocumentStudent relatedDocumentStudent = null;
        Optional<RelatedDocumentStudent> optional = relatedDocumentStudentRepository.findByStudentAndRelatedDocuments(studentRegis.get().getId(), relatedDocuments.getId());
        if(optional.isPresent()){
            relatedDocumentStudent = optional.get();
        }
        else{
            relatedDocumentStudent = new RelatedDocumentStudent();
            if(LocalDateTime.now().isBefore(relatedDocuments.getDeadline())){
                relatedDocuments.setOnTimeCount(relatedDocuments.getOnTimeCount() + 1);
            }
            else{
                relatedDocuments.setOutTimeCount(relatedDocuments.getOutTimeCount() + 1);
            }
        }

        relatedDocumentStudent.setRelatedDocuments(relatedDocuments);
        relatedDocumentStudent.setStudentRegis(studentRegis.get());
        relatedDocumentStudent.setFileUrl(request.getFileUrl());
        relatedDocumentStudent.setFileType(request.getFileType());
        relatedDocumentStudent.setFileName(request.getFileName());
        relatedDocumentsRepository.save(relatedDocuments);
        return relatedDocumentStudentRepository.save(relatedDocumentStudent);
    }
}
