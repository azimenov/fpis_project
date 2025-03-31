package org.example.fpis_project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDto {

    private Long id;

    private String name;

    private BigDecimal lowestPrice;

    private BigDecimal highestPrice;

    private Integer duration;

    private String topic;

    private List<String> staffNames;

    private Long businessId;
}
