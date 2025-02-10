package org.example.fpis_project.service;

import org.example.fpis_project.entity.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    List<Booking> getAllBookingsByUserId(Long id);

    Optional<Booking> getBookingById(Long id);

    Booking createBooking(Booking booking);

    void deleteBooking(Long id);
}
