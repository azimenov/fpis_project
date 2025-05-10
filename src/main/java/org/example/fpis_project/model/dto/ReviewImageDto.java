package org.example.fpis_project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewImageDto {
    private Long id;
    private Long reviewId;
    private String contentType;
    private String fileName;
}