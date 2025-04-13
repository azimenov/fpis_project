package org.example.fpis_project.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffDto {

    private Long id;

    @NotNull
    private String name;
    private String surname;

    @NotNull
    private String position;
    private String phone;
    private String description;

    @NotNull
    private Long businessId;

    private List<ServiceDto> services;
}
