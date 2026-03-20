package com.web.service;

import com.web.dto.request.SemesterCompanyRequest;
import com.web.entity.Company;
import com.web.entity.Semester;
import com.web.entity.SemesterCompany;
import com.web.exception.MessageException;
import com.web.repository.CompanyRepository;
import com.web.repository.SemesterCompanyRepository;
import com.web.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SemesterCompanyService {

    @Autowired
    private SemesterCompanyRepository semesterCompanyRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private CompanyRepository companyRepository;


    @Transactional
    public Map<String, String> save(SemesterCompanyRequest request){
        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new MessageException("Không tìm thấy học kỳ"));
        // insert company
        int check = 0;
        for(SemesterCompanyRequest.SemesterCompanyInsert i : request.getSemesterCompanyInserts()){

            Company company = companyRepository.findById(i.getCompanyId())
                    .orElseThrow(() -> new MessageException("Không tìm thấy công ty"));

            Optional<SemesterCompany> optional = semesterCompanyRepository.findBySemesterAndCompany(request.getSemesterId(), i.getCompanyId());
            if(optional.isPresent()){
                ++ check;
                continue;
            }
            SemesterCompany semesterCompany = new SemesterCompany();
            semesterCompany.setSemester(semester);
            semesterCompany.setCompany(company);
            semesterCompany.setDescription(i.getDescription());
            semesterCompany.setMaxStudent(i.getMaxStudent());
            semesterCompany.setCurrentStudent(0);

            semesterCompanyRepository.save(semesterCompany);
        }
        Map<String, String> map = new HashMap<>();
        map.put("message", "Thêm thành công");
        map.put("existCompany",String.valueOf(check));
        return map;
    }


    public Map<String,String> delete(Long id){
        try{
            semesterCompanyRepository.deleteById(id);
            return Map.of("message","Xóa công ty thành công");
        }
        catch (Exception e){
            throw new MessageException("Không thể xóa công ty");
        }
    }

    public SemesterCompany findById(Long id) {
        return semesterCompanyRepository.findById(id).orElseThrow(()->new MessageException("Không tìm thấy dữ liệu nào"));
    }

    public Map<String, String> update(SemesterCompanyRequest.SemesterCompanyUpdate request) {
        SemesterCompany semesterCompany = semesterCompanyRepository.findById(request.getId()).orElseThrow(() -> new MessageException("Không tìm thấy dữ liệu"));
        semesterCompany.setMaxStudent(request.getMaxStudent());
        semesterCompany.setDescription(request.getDescription());
        semesterCompanyRepository.save(semesterCompany);
        return Map.of("message", "Cập nhật thành công");
    }

    public Page<SemesterCompany> findAll(String search, Long semesterId, Pageable pageable){

        if(semesterId == null){
            throw new MessageException("semesterId không được để trống");
        }

        if(search == null || search.trim().isEmpty()){
            return semesterCompanyRepository.findBySemesterId(semesterId, pageable);
        }
        else{
            search = "%" + search + "%";
            return semesterCompanyRepository.findByParamAndSemester(search, semesterId, pageable);
        }
    }

}