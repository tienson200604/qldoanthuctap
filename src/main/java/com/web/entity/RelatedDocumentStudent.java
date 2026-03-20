package com.web.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "related_document_student")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class RelatedDocumentStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    private String fileUrl;

    private String fileName;

    private String fileType;

    @ManyToOne
    private StudentRegis studentRegis;

    @ManyToOne
    private RelatedDocuments relatedDocuments;
}
