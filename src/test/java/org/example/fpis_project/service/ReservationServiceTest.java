package org.example.fpis_project.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.fpis_project.model.dto.Reservation;
import org.example.fpis_project.model.dto.ReservationStatus;
import org.example.fpis_project.model.entity.Business;
import org.example.fpis_project.model.entity.ReservationDto;
import org.example.fpis_project.model.entity.Service;
import org.example.fpis_project.model.entity.Staff;
import org.example.fpis_project.repository.BusinessRepository;
import org.example.fpis_project.repository.ReservationRepository;
import org.example.fpis_project.repository.ServiceRepository;
import org.example.fpis_project.repository.StaffRepository;
import org.example.fpis_project.service.impl.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private BusinessRepository businessRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Business testBusiness;
    private Service testService;
    private Staff testStaff;
    private Reservation testReservation;
    private ReservationDto testReservationDto;
    private LocalDate testDate;
    private LocalDateTime testStartTime;
    private LocalDateTime testEndTime;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2025, 4, 15);
        testStartTime = LocalDateTime.of(testDate, LocalTime.of(10, 0));
        testEndTime = LocalDateTime.of(testDate, LocalTime.of(11, 0));

        testBusiness = Business.builder()
                .id(1L)
                .name("Test Business")
                .build();

        testService = Service.builder()
                .id(1L)
                .name("Test Service")
                .duration(60)
                .build();

        testStaff = Staff.builder()
                .id(1L)
                .name("Test Staff")
                .services(Collections.singletonList(testService))
                .build();

        testReservation = Reservation.builder()
                .id(1L)
                .service(testService)
                .staff(testStaff)
                .business(testBusiness)
                .customerName("Test Customer")
                .customerEmail("test@example.com")
                .customerPhone("123-456-7890")
                .startTime(testStartTime)
                .endTime(testEndTime)
                .status(ReservationStatus.CONFIRMED)
                .notes("Test notes")
                .build();

        testReservationDto = ReservationDto.builder()
                .id(1L)
                .serviceId(1L)
                .staffId(1L)
                .businessId(1L)
                .customerName("Test Customer")
                .customerEmail("test@example.com")
                .customerPhone("123-456-7890")
                .startTime(testStartTime)
                .status(ReservationStatus.CONFIRMED)
                .notes("Test notes")
                .build();
    }

    @Test
    void testGetStaffAvailabilityWhenReservationsExist() {
        LocalDateTime startOfDay = testDate.atStartOfDay();
        LocalDateTime endOfDay = testDate.plusDays(1).atStartOfDay();
        List<Reservation> reservations = Collections.singletonList(testReservation);

        when(reservationRepository.findByStaffIdAndStartTimeBetween(1L, startOfDay, endOfDay))
                .thenReturn(reservations);

        List<ReservationDto> result = reservationService.getStaffAvailability(1L, testDate);

        assertEquals(1, result.size());
        assertEquals(testReservation.getId(), result.get(0).getId());
        assertEquals(testReservation.getService().getId(), result.get(0).getServiceId());
        verify(reservationRepository, times(1)).findByStaffIdAndStartTimeBetween(1L, startOfDay, endOfDay);
    }

    @Test
    void testGetStaffAvailabilityWhenNoReservations() {
        LocalDateTime startOfDay = testDate.atStartOfDay();
        LocalDateTime endOfDay = testDate.plusDays(1).atStartOfDay();

        when(reservationRepository.findByStaffIdAndStartTimeBetween(1L, startOfDay, endOfDay))
                .thenReturn(Collections.emptyList());

        List<ReservationDto> result = reservationService.getStaffAvailability(1L, testDate);

        assertTrue(result.isEmpty());
        verify(reservationRepository, times(1)).findByStaffIdAndStartTimeBetween(1L, startOfDay, endOfDay);
    }

    @Test
    void testCreateReservationWhenAllEntitiesExistAndTimeSlotAvailable() {
        when(businessRepository.findById(1L)).thenReturn(Optional.of(testBusiness));
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(testService));
        when(staffRepository.findById(1L)).thenReturn(Optional.of(testStaff));
        when(reservationRepository.findByStaffIdAndStartTimeBetween(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        ReservationDto result = reservationService.createReservation(testReservationDto);

        assertNotNull(result);
        assertEquals(testReservation.getId(), result.getId());
        verify(businessRepository).findById(1L);
        verify(serviceRepository).findById(1L);
        verify(staffRepository).findById(1L);
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void testCreateReservationWhenBusinessNotFound() {
        when(businessRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reservationService.createReservation(testReservationDto));
        verify(businessRepository).findById(1L);
        verify(serviceRepository, never()).findById(anyLong());
        verify(staffRepository, never()).findById(anyLong());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testCreateReservationWhenServiceNotFound() {
        when(businessRepository.findById(1L)).thenReturn(Optional.of(testBusiness));
        when(serviceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reservationService.createReservation(testReservationDto));
        verify(businessRepository).findById(1L);
        verify(serviceRepository).findById(1L);
        verify(staffRepository, never()).findById(anyLong());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testCreateReservationWhenStaffNotFound() {
        when(businessRepository.findById(1L)).thenReturn(Optional.of(testBusiness));
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(testService));
        when(staffRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reservationService.createReservation(testReservationDto));
        verify(businessRepository).findById(1L);
        verify(serviceRepository).findById(1L);
        verify(staffRepository).findById(1L);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testCreateReservationWhenStaffDoesNotProvideService() {
        Staff staffWithoutService = Staff.builder()
                .id(1L)
                .name("Test Staff")
                .services(new ArrayList<>())
                .build();

        when(businessRepository.findById(1L)).thenReturn(Optional.of(testBusiness));
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(testService));
        when(staffRepository.findById(1L)).thenReturn(Optional.of(staffWithoutService));

        assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(testReservationDto));
        verify(businessRepository).findById(1L);
        verify(serviceRepository).findById(1L);
        verify(staffRepository).findById(1L);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testCreateReservationWhenTimeSlotNotAvailable() {
        LocalDateTime overlapStart = testStartTime.minusMinutes(30);
        LocalDateTime overlapEnd = testStartTime.plusMinutes(30);

        Reservation existingReservation = Reservation.builder()
                .id(2L)
                .staff(testStaff)
                .startTime(overlapStart)
                .endTime(overlapEnd)
                .build();

        List<Reservation> conflictingReservations = Collections.singletonList(existingReservation);

        when(businessRepository.findById(1L)).thenReturn(Optional.of(testBusiness));
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(testService));
        when(staffRepository.findById(1L)).thenReturn(Optional.of(testStaff));
        when(reservationRepository.findByStaffIdAndStartTimeBetween(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(conflictingReservations);

        assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(testReservationDto));
        verify(businessRepository).findById(1L);
        verify(serviceRepository).findById(1L);
        verify(staffRepository).findById(1L);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testGetAvailableTimeSlotsWhenServiceExistsAndSlotsAvailable() {
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(testService));
        when(reservationRepository.findByStaffIdAndStartTimeBetween(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        List<ReservationDto> result = reservationService.getAvailableTimeSlots(1L, 1L, 1L, testDate);

        assertFalse(result.isEmpty());
        assertEquals(1L, result.get(0).getServiceId());
        assertEquals(1L, result.get(0).getStaffId());
        assertEquals(1L, result.get(0).getBusinessId());
        verify(serviceRepository).findById(1L);
        verify(reservationRepository).findByStaffIdAndStartTimeBetween(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void testGetAvailableTimeSlotsWhenServiceNotFound() {
        when(serviceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> reservationService.getAvailableTimeSlots(1L, 1L, 1L, testDate));
        verify(serviceRepository).findById(1L);
        verify(reservationRepository, never()).findByStaffIdAndStartTimeBetween(
                anyLong(), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void testGetAvailableTimeSlotsWhenExistingReservationsLimitAvailability() {
        LocalDateTime reservedStart = LocalDateTime.of(testDate, LocalTime.of(10, 0));
        LocalDateTime reservedEnd = LocalDateTime.of(testDate, LocalTime.of(11, 0));

        Reservation existingReservation = Reservation.builder()
                .id(2L)
                .staff(testStaff)
                .startTime(reservedStart)
                .endTime(reservedEnd)
                .build();

        List<Reservation> existingReservations = Collections.singletonList(existingReservation);

        when(serviceRepository.findById(1L)).thenReturn(Optional.of(testService));
        when(reservationRepository.findByStaffIdAndStartTimeBetween(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(existingReservations);

        List<ReservationDto> result = reservationService.getAvailableTimeSlots(1L, 1L, 1L, testDate);

        for (ReservationDto slot : result) {
            assertFalse(slot.getStartTime().equals(reservedStart),
                    "Reserved time slot should not be included in available slots");
        }

        verify(serviceRepository).findById(1L);
        verify(reservationRepository).findByStaffIdAndStartTimeBetween(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void testGetCurrentReservationsWhenReservationsExist() {
        String email = "test@example.com";
        List<Reservation> upcomingReservations = Collections.singletonList(testReservation);

        when(reservationRepository.findByCustomerEmailAndStartTimeGreaterThanEqualOrderByStartTimeAsc(
                eq(email), any(LocalDateTime.class)))
                .thenReturn(upcomingReservations);

        List<ReservationDto> result = reservationService.getCurrentReservations(email);

        assertEquals(1, result.size());
        assertEquals(testReservation.getId(), result.get(0).getId());
        assertEquals(email, result.get(0).getCustomerEmail());
        verify(reservationRepository).findByCustomerEmailAndStartTimeGreaterThanEqualOrderByStartTimeAsc(
                eq(email), any(LocalDateTime.class));
    }

    @Test
    void testGetCurrentReservationsWhenNoReservations() {
        String email = "test@example.com";

        when(reservationRepository.findByCustomerEmailAndStartTimeGreaterThanEqualOrderByStartTimeAsc(
                eq(email), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        List<ReservationDto> result = reservationService.getCurrentReservations(email);

        assertTrue(result.isEmpty());
        verify(reservationRepository).findByCustomerEmailAndStartTimeGreaterThanEqualOrderByStartTimeAsc(
                eq(email), any(LocalDateTime.class));
    }

    @Test
    void testGetReservationHistoryWhenReservationsExist() {
        String email = "test@example.com";
        List<Reservation> pastReservations = Collections.singletonList(testReservation);

        when(reservationRepository.findByCustomerEmailAndStartTimeLessThanOrderByStartTimeDesc(
                eq(email), any(LocalDateTime.class)))
                .thenReturn(pastReservations);

        List<ReservationDto> result = reservationService.getReservationHistory(email);

        assertEquals(1, result.size());
        assertEquals(testReservation.getId(), result.get(0).getId());
        assertEquals(email, result.get(0).getCustomerEmail());
        verify(reservationRepository).findByCustomerEmailAndStartTimeLessThanOrderByStartTimeDesc(
                eq(email), any(LocalDateTime.class));
    }

    @Test
    void testGetReservationHistoryWhenNoReservations() {
        String email = "test@example.com";

        when(reservationRepository.findByCustomerEmailAndStartTimeLessThanOrderByStartTimeDesc(
                eq(email), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        List<ReservationDto> result = reservationService.getReservationHistory(email);

        assertTrue(result.isEmpty());
        verify(reservationRepository).findByCustomerEmailAndStartTimeLessThanOrderByStartTimeDesc(
                eq(email), any(LocalDateTime.class));
    }
}