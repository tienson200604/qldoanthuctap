package com.web.service;

import com.web.entity.Company;
import com.web.exception.MessageException;
import com.web.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public Company save(Company company) {
        if(company.getId() == null){
            if(companyRepository.existsByName(company.getName())){
                throw new MessageException("Tên công ty đã tồn tại");
            }
        }
        else{
            Company old = companyRepository.findById(company.getId())
                    .orElseThrow(() -> new MessageException("Không tìm thấy công ty"));

            if(!old.getName().equals(company.getName())){
                if(companyRepository.existsByName(company.getName())){
                    throw new MessageException("Tên công ty đã tồn tại");
                }
            }
        }

        return companyRepository.save(company);
    }

    public Map<String, String> delete(Long id) {
        try{
            companyRepository.deleteById(id);
            return Map.of("message","Xóa công ty thành công");
        }
        catch (Exception e){
            throw new MessageException("Công ty đã có liên kết, không thể xóa");
        }
    }

    public Company findById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new MessageException("Không tìm thấy công ty"));
    }

    public Page<Company> findAll(String search,Pageable pageable) {
        if(search == null){
            return companyRepository.findAll(pageable);
        }
        else{
            search = "%"+search+"%";
            return companyRepository.findByParam(search,pageable);
        }
    }
}
