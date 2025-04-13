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
public class BusinessDto {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String address;

    @NotNull
    private String phone;

    @NotNull
    private String description;

    @NotNull
    private String topic;

    private List<ServiceDto> services;
}
