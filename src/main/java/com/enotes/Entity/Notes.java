package com.enotes.Entity;


import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Notes extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @Lob
    private String description;

    @ManyToOne
    private Category category;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_details_id",referencedColumnName = "id")
    private FileDetails fileDetails;

    private Boolean isDeleted;

    private LocalDateTime deletedOn;


}
