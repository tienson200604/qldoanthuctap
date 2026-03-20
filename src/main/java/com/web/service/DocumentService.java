package com.web.service;

import com.web.dto.request.DocumentRequest;
import com.web.entity.Category;
import com.web.entity.Document;
import com.web.entity.DocumentDetail;
import com.web.entity.User;
import com.web.enums.DocumentStatus;
import com.web.exception.MessageException;
import com.web.mapper.DocumentMapper;
import com.web.repository.CategoryRepository;
import com.web.repository.DocumentDetailRepository;
import com.web.repository.DocumentRepository;
import com.web.utils.Contains;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentDetailRepository documentDetailRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private DocumentMapper documentMapper;

    public Document saveOrUpdate(DocumentRequest dto){
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(()-> new MessageException("Không tìm thấy danh mục với id: "+dto.getCategoryId()));
        Document document = documentMapper.requestToEntity(dto);
        document.setCategory(category);
        User user = userUtils.getUserWithAuthority();
        document.setUser(user);
        if(document.getId() == null){
            if(!user.getAuthorities().getName().equals(Contains.ROLE_ADMIN) && dto.getStatus() != null){
                throw new MessageException("Bạn không có quyền set status cho tài liệu, hãy bỏ field tài liệu hoặc để null");
            }
        }
        else{
            Document optional = documentRepository.findById(dto.getId()).orElseThrow(() -> new MessageException("Không tìm thấy tài liệu với id: "+dto.getId()));
            if(!user.getAuthorities().getName().equals(Contains.ROLE_ADMIN) && !user.getAuthorities().getName().equals(Contains.ROLE_STUDENT)){
                if(!optional.getUser().getId().equals(user.getId())){
                    throw new MessageException("Chỉ admin và chủ tài liệu mới có quyền chỉnh sửa tài liệu này");
                }
            }
            if(!user.getAuthorities().getName().equals(Contains.ROLE_ADMIN)){
                document.setStatus(optional.getStatus());
            }
            document.setNumberDownload(optional.getNumberDownload());
            document.setNumberView(optional.getNumberView());
            document.setUser(optional.getUser());
            document.setStatus(optional.getStatus());
        }
        if(document.getStatus() == null){
            document.setStatus(DocumentStatus.DANG_CHO);
        }
        else{
            document.setStatus(dto.getStatus());
        }
        Document result = documentRepository.save(document);
        for(DocumentRequest.Detail s : dto.getDetails()){
            DocumentDetail detail = documentMapper.requestToEntity(s);
            detail.setDocument(document);
            documentDetailRepository.save(detail);
        }
        return result;
    }

    public Page<Document> searchAll(String search, Long categoryId, Pageable pageable){
        Page<Document> page = null;
        page = documentRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (search != null && !search.isEmpty()) {
                String pattern = "%" + search.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), pattern)
                ));
            }
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }
            predicates.add(cb.equal(root.get("status"), DocumentStatus.DANG_HIEN_THI));
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
        return page;
    }

    public Page<Document> searchAllByAdmin(String search, Long categoryId, DocumentStatus status, Pageable pageable){
        Page<Document> page = null;
        page = documentRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (search != null && !search.isEmpty()) {
                String pattern = "%" + search.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), pattern)
                ));
            }
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
        return page;
    }

    public Page<Document> myDocument(String search, Long categoryId, DocumentStatus status, Pageable pageable){
        Page<Document> page = null;
        User user = userUtils.getUserWithAuthority();
        page = documentRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (search != null && !search.isEmpty()) {
                String pattern = "%" + search.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), pattern)
                ));
            }
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            predicates.add(cb.equal(root.get("user").get("id"), user.getId()));
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
        return page;
    }

    public Page<Document> myDocument(Pageable pageable){
        User user = userUtils.getUserWithAuthority();
        Page<Document> page = documentRepository.findByUserId(user.getId(),pageable);
        return page;
    }

    public Document findByIdPublic(Long id){
        Document document = documentRepository.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy tài liệu với id: "+id));
        if(!document.getStatus().equals(DocumentStatus.DANG_HIEN_THI)){
            throw new MessageException("Tài liệu không thể hiển thị");
        }
        return document;
    }

    public void upDownload(Long id){
        Document document = documentRepository.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy tài liệu với id: "+id));
        if(!document.getStatus().equals(DocumentStatus.DANG_HIEN_THI)){
            throw new MessageException("Tài liệu không thể hiển thị");
        }
        if(document.getNumberDownload() == null){
            document.setNumberDownload(0);
        }
        document.setNumberDownload(document.getNumberDownload() + 1);
        documentRepository.save(document);
    }

    public Document findByPrivate(Long id){
        User user = userUtils.getUserWithAuthority();
        Document document = documentRepository.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy tài liệu với id: "+id));
        if(user.getAuthorities().getName().equals(Contains.ROLE_ADMIN)){
            return document;
        }
        else{
            if(!user.getId().equals(document.getUser().getId())){
                throw new MessageException("Bạn không có quyền xem tài liệu này");
            }
            return document;
        }
    }

    public Map<String, String> delete(Long id){
        User user = userUtils.getUserWithAuthority();
        Document document = documentRepository.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy tài liệu với id: "+id));
        if(user.getAuthorities().getName().equals(Contains.ROLE_ADMIN)){
            documentRepository.deleteById(id);
        }
        else{
            if(!user.getId().equals(document.getUser().getId())){
                throw new MessageException("Bạn không có quyền xóa tài liệu này");
            }
            documentRepository.deleteById(id);
        }
        Map<String, String> map = new HashMap<>();
        map.put("message", "Xóa tài liệu thành công");
        return map;
    }

    public Map<String, Object> updateStatus(Long id, DocumentStatus status){
        Document document = documentRepository.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy tài liệu với id: "+id));
        document.setStatus(status);
        documentRepository.save(document);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Cập nhật trạng thái liệu thành công");
        map.put("data", document);
        return map;
    }

    public Map<String, String> deleteDetail(Long idDetail) {
        documentDetailRepository.deleteById(idDetail);
        return Map.of("message","Xóa file thành công");
    }
}
