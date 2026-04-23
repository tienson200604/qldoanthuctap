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

    @Autowired
    private NotificationService notificationService;

    public Document saveOrUpdate(DocumentRequest dto){
        if (dto.getCategoryId() == null) {
            throw new MessageException("Vui lòng chọn danh mục cho tài liệu");
        }
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(()-> new MessageException("Không tìm thấy danh mục với id: "+dto.getCategoryId()));
        
        Document document;
        User user = userUtils.getUserWithAuthority();
        
        if(dto.getId() == null){
            document = new Document();
            document.setNumberDownload(0);
            document.setNumberView(0);
            document.setUser(user);
            if(!user.getAuthorities().getName().equals(Contains.ROLE_ADMIN) && dto.getStatus() != null){
                throw new MessageException("Bạn không có quyền set status cho tài liệu");
            }
            document.setStatus(dto.getStatus() != null ? dto.getStatus() : DocumentStatus.DANG_CHO);
        }
        else{
            document = documentRepository.findById(dto.getId()).orElseThrow(() -> new MessageException("Không tìm thấy tài liệu với id: "+dto.getId()));
            if(!user.getAuthorities().getName().equals(Contains.ROLE_ADMIN) && !user.getAuthorities().getName().equals(Contains.ROLE_STUDENT)){
                if(!document.getUser().getId().equals(user.getId())){
                    throw new MessageException("Chỉ admin và chủ tài liệu mới có quyền chỉnh sửa tài liệu này");
                }
            }
            if(user.getAuthorities().getName().equals(Contains.ROLE_ADMIN) && dto.getStatus() != null){
                document.setStatus(dto.getStatus());
            }
        }

        // Cập nhật các trường thông tin từ DTO
        document.setName(dto.getName());
        document.setDescription(dto.getDescription());
        document.setLinkImage(dto.getLinkImage());
        document.setCategory(category);
        
        Document result = documentRepository.save(document);
        
        if(DocumentStatus.DANG_CHO.equals(result.getStatus())){
            notificationService.save("Tài liệu mới", "/admin/document?status=DANG_CHO", "Có tài liệu mới đang chờ duyệt: "+result.getName());
        }
        
        if (dto.getDetails() != null) {
            for(DocumentRequest.Detail s : dto.getDetails()){
                DocumentDetail detail = documentMapper.requestToEntity(s);
                detail.setDocument(result);
                documentDetailRepository.save(detail);
            }
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
        if(status.equals(DocumentStatus.DANG_HIEN_THI)){
            notificationService.saveToAll("Tài liệu mới", "/student/document-detail?id="+document.getId(), "Có tài liệu mới: "+document.getName());
        }
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
