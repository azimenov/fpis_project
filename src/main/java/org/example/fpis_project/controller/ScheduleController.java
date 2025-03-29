package org.example.fpis_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.BookingRequest;
import org.example.fpis_project.model.entity.Booking;
import org.example.fpis_project.model.entity.ScheduleSlot;
import org.example.fpis_project.service.impl.BookingService;
import org.example.fpis_project.service.impl.ScheduleSlotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleSlotService scheduleSlotService;
    private final BookingService bookingService;

    @GetMapping("/staff/{staffId}/slots")
    public ResponseEntity<List<ScheduleSlot>> getSlots(
            @PathVariable Long staffId
    ) {
        List<ScheduleSlot> slots = scheduleSlotService.findAvailableSlots(staffId);
        return ResponseEntity.ok(slots);
    }

    @PostMapping("/bookings")
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request) {
        Booking booking = bookingService.bookSlot(request.getUserId(), request.getSlotId());
        return ResponseEntity.ok(booking);
    }
}
