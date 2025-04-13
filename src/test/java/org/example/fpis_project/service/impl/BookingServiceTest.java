package org.example.fpis_project.service.impl;

import org.example.fpis_project.model.entity.Service;
import org.example.fpis_project.model.entity.Staff;
import org.example.fpis_project.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ScheduleSlotRepository scheduleSlotRepository;

    @InjectMocks
    private BookingService bookingService;

    private Long userId;
    private Long slotId;
    private ScheduleSlot scheduleSlot;
    private User user;
    private Staff staff;
    private Service service;
    private Booking expectedBooking;

    @BeforeEach
    void setUp() {
        userId = 1L;
        slotId = 2L;

        user = User.builder().id(userId).build();

        staff = Staff.builder().id(3L).build();

        service = Service.builder().id(4L).build();

        scheduleSlot = ScheduleSlot.builder()
                .id(slotId)
                .staff(staff)
                .service(service)
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(2))
                .scheduled(false)
                .build();

        expectedBooking = new Booking();
        expectedBooking.setId(5L);
        expectedBooking.setUser(user);
        expectedBooking.setScheduleSlot(scheduleSlot);
    }

    @Test
    void bookSlot_ShouldBookAvailableSlot() {
        // Arrange
        when(scheduleSlotRepository.findById(slotId)).thenReturn(Optional.of(scheduleSlot));
        when(bookingRepository.save(any(Booking.class))).thenReturn(expectedBooking);

        // Act
        Booking actualBooking = bookingService.bookSlot(userId, slotId);

        // Assert
        assertEquals(expectedBooking, actualBooking);
        assertTrue(scheduleSlot.getScheduled());
        verify(scheduleSlotRepository).findById(slotId);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void bookSlot_ShouldThrowException_WhenSlotNotFound() {
        // Arrange
        when(scheduleSlotRepository.findById(slotId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bookingService.bookSlot(userId, slotId)
        );

        assertEquals("Slot not found", exception.getMessage());
        verify(scheduleSlotRepository).findById(slotId);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void bookSlot_ShouldThrowException_WhenSlotAlreadyBooked() {
        // Arrange
        scheduleSlot.setScheduled(true);
        when(scheduleSlotRepository.findById(slotId)).thenReturn(Optional.of(scheduleSlot));

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bookingService.bookSlot(userId, slotId)
        );

        assertEquals("Slot already booked", exception.getMessage());
        verify(scheduleSlotRepository).findById(slotId);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void bookSlot_ShouldCreateBookingWithCorrectData() {
        // Arrange
        when(scheduleSlotRepository.findById(slotId)).thenReturn(Optional.of(scheduleSlot));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking savedBooking = invocation.getArgument(0);
            savedBooking.setId(5L);
            return savedBooking;
        });

        // Act
        Booking actualBooking = bookingService.bookSlot(userId, slotId);

        // Assert
        assertEquals(userId, actualBooking.getUser().getId());
        assertEquals(slotId, actualBooking.getScheduleSlot().getId());
        assertTrue(actualBooking.getScheduleSlot().getScheduled());
        verify(scheduleSlotRepository).findById(slotId);
        verify(bookingRepository).save(any(Booking.class));
    }
}