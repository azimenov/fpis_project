package org.example.fpis_project.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BusinessImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = true)
    private Business business;

    @ManyToOne
    @JoinColumn(name = "business_application_id", nullable = true)
    private BusinessApplication businessApplication;

    @Lob
    @Column(columnDefinition = "bytea")
    private byte[] imageData;

    private String contentType;

    private String fileName;
}