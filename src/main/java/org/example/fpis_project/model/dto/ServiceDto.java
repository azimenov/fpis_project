package org.example.fpis_project.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServiceDto {

    private String name;

    private BigDecimal lowestPrice;

    private BigDecimal highestPrice;

    private Integer duration;

    private String topic;

    private String staffName;

    private Long businessId;
}
