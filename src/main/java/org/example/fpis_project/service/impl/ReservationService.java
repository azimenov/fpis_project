package org.example.fpis_project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ServiceRepository serviceRepository;
    private final StaffRepository staffRepository;
    private final BusinessRepository businessRepository;
    private final NotificationService notificationService;

    public List<ReservationDto> getStaffAvailability(Long staffId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        List<Reservation> existingReservations = reservationRepository.findByStaffIdAndStartTimeBetween(
                staffId, startOfDay, endOfDay);

        return existingReservations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ReservationDto createReservation(ReservationDto reservationDto) {
        Business business = businessRepository.findById(reservationDto.getBusinessId())
                .orElseThrow(() -> new EntityNotFoundException("Business not found: " + reservationDto.getBusinessId()));

        Service service = serviceRepository.findById(reservationDto.getServiceId())
                .orElseThrow(() -> new EntityNotFoundException("Service not found: " + reservationDto.getServiceId()));

        Staff staff = staffRepository.findById(reservationDto.getStaffId())
                .orElseThrow(() -> new EntityNotFoundException("Staff not found: " + reservationDto.getStaffId()));

        if (!staff.getServices().contains(service)) {
            throw new IllegalArgumentException("Staff does not provide the requested service");
        }

        LocalDateTime startTime = reservationDto.getStartTime();
        LocalDateTime endTime = startTime.plusMinutes(service.getDuration());

        if (!isTimeSlotAvailable(staff.getId(), startTime, endTime)) {
            throw new IllegalArgumentException("Selected time slot is not available");
        }

        Reservation reservation = Reservation.builder()
                .service(service)
                .staff(staff)
                .business(business)
                .customerName(reservationDto.getCustomerName())
                .customerEmail(reservationDto.getCustomerEmail())
                .customerPhone(reservationDto.getCustomerPhone())
                .startTime(startTime)
                .endTime(endTime)
                .status(ReservationStatus.CONFIRMED)
                .notes(reservationDto.getNotes())
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);
        notificationService.sendSimpleMessage(reservationDto.getCustomerEmail(), "reservation created", "your reservation created");
        return convertToDto(savedReservation);
    }

    public List<ReservationDto> getAvailableTimeSlots(Long businessId, Long serviceId, Long staffId, LocalDate date) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("Service not found: " + serviceId));

        LocalTime businessStart = LocalTime.of(9, 0);
        LocalTime businessEnd = LocalTime.of(18, 0);

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        List<Reservation> existingReservations = reservationRepository.findByStaffIdAndStartTimeBetween(
                staffId, startOfDay, endOfDay);

        List<ReservationDto> availableSlots = new ArrayList<>();
        int serviceDuration = service.getDuration();

        LocalDateTime currentSlot = date.atTime(businessStart);
        LocalDateTime businessCloseTime = date.atTime(businessEnd);

        while (currentSlot.plusMinutes(serviceDuration).isBefore(businessCloseTime) ||
                currentSlot.plusMinutes(serviceDuration).isEqual(businessCloseTime)) {

            LocalDateTime slotEndTime = currentSlot.plusMinutes(serviceDuration);
            boolean isAvailable = true;

            for (Reservation reservation : existingReservations) {
                if (currentSlot.isBefore(reservation.getEndTime()) &&
                        slotEndTime.isAfter(reservation.getStartTime())) {
                    isAvailable = false;
                    break;
                }
            }

            if (isAvailable) {
                ReservationDto slot = ReservationDto.builder()
                        .serviceId(serviceId)
                        .staffId(staffId)
                        .businessId(businessId)
                        .startTime(currentSlot)
                        .build();
                availableSlots.add(slot);
            }

            currentSlot = currentSlot.plusMinutes(30);
        }

        return availableSlots;
    }

    private ReservationDto convertToDto(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .serviceId(reservation.getService().getId())
                .staffId(reservation.getStaff().getId())
                .businessId(reservation.getBusiness().getId())
                .customerName(reservation.getCustomerName())
                .customerEmail(reservation.getCustomerEmail())
                .customerPhone(reservation.getCustomerPhone())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .status(reservation.getStatus())
                .notes(reservation.getNotes())
                .build();
    }

    private boolean isTimeSlotAvailable(Long staffId, LocalDateTime start, LocalDateTime end) {
        List<Reservation> existingReservations = reservationRepository.findByStaffIdAndStartTimeBetween(
                staffId, start.minusMinutes(1), end.plusMinutes(1));

        return existingReservations.stream().noneMatch(reservation ->
                (start.isBefore(reservation.getEndTime()) && end.isAfter(reservation.getStartTime())));
    }

    public List<ReservationDto> getCurrentReservations(String phone) {
        LocalDateTime now = LocalDateTime.now();

        List<Reservation> upcomingReservations = reservationRepository
                .findByCustomerPhone(phone);

        return upcomingReservations.stream()
                .filter(reservation -> reservation.getEndTime().isAfter(now))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ReservationDto> getReservationHistory(String phone) {
        LocalDateTime now = LocalDateTime.now();

        List<Reservation> pastReservations = reservationRepository
                .findByCustomerPhone(phone);

        return pastReservations.stream()
                .filter(reservation -> reservation.getEndTime().isBefore(now))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}