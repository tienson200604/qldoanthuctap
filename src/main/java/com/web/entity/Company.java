package com.web.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "company")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @CreatedDate
    @Column(updatable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime createdDate;

    private String imageBanner;

    private String address;

    private String phone;

    private String email;

    private String website;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String taxCode;

    private Boolean active = true;
}
