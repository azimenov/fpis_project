package org.example.fpis_project.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.entity.ReservationDto;
import org.example.fpis_project.service.impl.ReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationDto> createReservation(@Valid @RequestBody ReservationDto reservationDto) {
        return ResponseEntity.ok(reservationService.createReservation(reservationDto));
    }

    @GetMapping("/available")
    public ResponseEntity<List<ReservationDto>> getAvailableTimeSlots(
            @RequestParam Long businessId,
            @RequestParam Long serviceId,
            @RequestParam Long staffId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(reservationService.getAvailableTimeSlots(
                businessId, serviceId, staffId, date));
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<ReservationDto>> getStaffReservations(
            @PathVariable Long staffId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return ResponseEntity.ok(reservationService.getStaffAvailability(staffId, date));
    }

    @GetMapping("/client/current")
    public ResponseEntity<List<ReservationDto>> getCurrentReservations(@RequestParam String phone) {
        return ResponseEntity.ok(reservationService.getCurrentReservations(phone));
    }

    @GetMapping("/client/history")
    public ResponseEntity<List<ReservationDto>> getReservationHistory(@RequestParam String phone) {
        return ResponseEntity.ok(reservationService.getReservationHistory(phone));
    }
}