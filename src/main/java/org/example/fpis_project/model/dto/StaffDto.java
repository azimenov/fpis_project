package org.example.fpis_project.model.dto;

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
    private String name;
    private String position;

    private Long businessId;

    private List<ServiceDto> services;
}
