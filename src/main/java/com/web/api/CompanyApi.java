package com.web.api;

import com.web.entity.Company;
import com.web.repository.CompanyRepository;
import com.web.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company")
public class CompanyApi {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    @PostMapping("/admin/create-update")
    public Company create(@RequestBody Company company){
        return companyService.save(company);
    }

    @GetMapping("/public/find-all")
    public Page<Company> getAll(Pageable pageable, @RequestParam(required = false) String search){
        return companyService.findAll(search, pageable);
    }

    @GetMapping("/public/find-all-list")
    public List<Company> getAll(){
        return companyRepository.findAllList();
    }

    @GetMapping("/public/find-by-id")
    public Company getById(@RequestParam Long id){
        return companyService.findById(id);
    }

    @DeleteMapping("/admin/delete")
    public void delete(@RequestParam Long id){
        companyService.delete(id);
    }
}
