package org.example.fpis_project.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.entity.Booking;
import org.example.fpis_project.model.entity.ScheduleSlot;
import org.example.fpis_project.model.entity.User;
import org.example.fpis_project.repository.BookingRepository;
import org.example.fpis_project.repository.ScheduleSlotRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ScheduleSlotRepository scheduleSlotRepository;

    @Transactional
    public Booking bookSlot(Long userId, Long slotId) {
        ScheduleSlot slot = scheduleSlotRepository
                .findById(slotId)
                .orElseThrow(
                        () -> new RuntimeException("Slot not found")
                );

        if (slot.getScheduled()) {
            throw new RuntimeException("Slot already booked");
        }

        slot.setScheduled(true);

        Booking booking = new Booking();
        booking.setUser(
                User.builder().id(userId).build()
        );
        booking.setScheduleSlot(slot);

        return bookingRepository.save(booking);
    }

}
