package org.example.fpis_project.model.dto;

import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private String name;

    @NotNull
    private BigDecimal lowestPrice;

    @NotNull
    private BigDecimal highestPrice;

    @NotNull
    private Integer duration;

    @NotNull
    private String topic;

    private List<Long> staffIds;

    @NotNull
    private Long businessId;
}
