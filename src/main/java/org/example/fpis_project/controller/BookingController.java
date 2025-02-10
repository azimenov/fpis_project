package org.example.fpis_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.fpis_project.entity.Booking;
import org.example.fpis_project.service.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/byUser/{userId}")
    public List<Booking> getAllBookingsByUser(
            @PathVariable Long userId
    ){
        return bookingService.getAllBookingsByUserId(userId);
    }

    @GetMapping("/{id}")
    public Optional<Booking> getBookingById(@PathVariable Long id){
        return bookingService.getBookingById(id);
    }

    @PostMapping
    public Booking createBooking(@RequestBody Booking booking){
        return bookingService.createBooking(booking);
    }

    @DeleteMapping("{id}")
    public void deleteBooking(@PathVariable Long id){
        bookingService.deleteBooking(id);
    }
}
