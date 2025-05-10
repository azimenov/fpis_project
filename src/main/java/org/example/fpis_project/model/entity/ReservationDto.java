package org.example.fpis_project.model.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.fpis_project.model.dto.ReservationStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    private Long id;

    @NotNull
    private Long serviceId;

    @NotNull
    private Long staffId;

    @NotNull
    private Long businessId;

    @NotBlank
    private String customerName;

    @Email
    private String customerEmail;

    private String customerPhone;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    private ReservationStatus status;

    private String notes;
}