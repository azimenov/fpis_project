package org.example.fpis_project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessDto {

    private Long id;

    private String name;

    private String address;

    private String phone;

    private List<ServiceDto> services;
}
