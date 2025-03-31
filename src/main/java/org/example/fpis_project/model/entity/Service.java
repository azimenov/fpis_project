package org.example.fpis_project.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private BigDecimal lowestPrice;

    private BigDecimal highestPrice;

    private Integer duration;

    private String topic;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;
}
