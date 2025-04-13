package org.example.fpis_project.repository;

import org.example.fpis_project.model.dto.Reservation;
import org.example.fpis_project.model.dto.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStaffIdAndStartTimeBetween(Long staffId, LocalDateTime start, LocalDateTime end);
    List<Reservation> findByBusinessIdAndStartTimeBetween(Long businessId, LocalDateTime start, LocalDateTime end);
    List<Reservation> findByStaffIdAndStatus(Long staffId, ReservationStatus status);
}