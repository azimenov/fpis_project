package org.example.fpis_project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BusinessImageDto {
    private Long id;
    private Long businessId;
    private Long businessApplicationId;
    private String contentType;
    private String fileName;
    private String dataUrl;
}