package org.example.fpis_project.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessApplicationDto {

    private Long id;

    private String ownerName;

    private String ownerSurname;

    private Long ownerId;

    private String phone;

    private String country;

    private String city;

    private String address;

    private String businessName;

    private String businessType;

    private String link;

    private String description;

    private boolean verified;

    private LocalDate createdAt;

}
