package org.example.fpis_project.entity;

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
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String position;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    private List<Booking> bookings;
}
