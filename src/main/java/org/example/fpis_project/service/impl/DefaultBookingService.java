package org.example.fpis_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.fpis_project.entity.Booking;
import org.example.fpis_project.repository.BookingRepository;
import org.example.fpis_project.service.BookingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultBookingService implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public List<Booking> getAllBookingsByUserId(Long userId) {
        return bookingRepository.findAllByUserId(userId);
    }

    @Override
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}
