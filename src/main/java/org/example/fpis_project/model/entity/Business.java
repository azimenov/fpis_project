package org.example.fpis_project.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String phone;

    private String description;

    private String topic;

    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    private List<Service> services;
}
