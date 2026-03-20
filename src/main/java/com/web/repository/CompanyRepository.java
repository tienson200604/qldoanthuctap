package com.web.repository;

import com.web.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByName(String name);

    boolean existsByName(String name);

    @Query("select c from Company c where c.name like ?1 or c.taxCode like ?1 or c.email like ?1 or c.address like ?1")
    Page<Company> findByParam(String search, Pageable pageable);

    @Query("select c from Company c where c.name like ?1 or c.taxCode like ?1 or c.email like ?1 or c.address like ?1")
    List<Company> findByParam(String search);

    @Query("select c from Company c where c.active = true")
    List<Company> findAllList();
}
