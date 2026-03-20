package com.web.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.web.enums.DocumentStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "document")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    @JsonFormat(pattern = "hh:mm dd/MM/YYYY")
    private LocalDateTime createdDate;

    private String name;

    private String linkImage;

    private Integer numberView = 0;

    private Integer numberDownload;

    private String description;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "document", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<DocumentDetail> documentDetails;
}
