package org.example.fpis_project.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessApplicationDto {

    private String ownerName;

    private String ownerSurname;

    private Long ownerId;

    private String phone;

    private String country;

    private String city;

    private String businessName;

    private String businessType;

    private String link;

    private String description;

}
