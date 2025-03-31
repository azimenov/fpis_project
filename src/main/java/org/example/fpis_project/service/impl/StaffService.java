package org.example.fpis_project.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.ServiceDto;
import org.example.fpis_project.model.dto.StaffDto;
import org.example.fpis_project.model.entity.Staff;
import org.example.fpis_project.model.entity.WorkingSchedule;
import org.example.fpis_project.repository.StaffRepository;
import org.example.fpis_project.repository.WorkingScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository repository;
    private final WorkingScheduleRepository workingScheduleRepository;
    private final StaffRepository staffRepository;

    public List<StaffDto> getServices(Long businessId) {
        return repository.findByBusinessId(businessId).stream()
                .map(this::mapToStaffDto)
                .collect(Collectors.toList());
    }

    private StaffDto mapToStaffDto(Staff staff) {
        return StaffDto.builder()
                .id(staff.getId())
                .name(staff.getName())
                .position(staff.getPosition())
                .businessId(staff.getBusiness().getId())
                .services(staff.getServices().stream().map(this::mapToServiceDto).collect(Collectors.toList()))
                .build();
    }

    private ServiceDto mapToServiceDto(org.example.fpis_project.model.entity.Service service) {
        return ServiceDto.builder()
                .name(service.getName())
                .lowestPrice(service.getLowestPrice())
                .highestPrice(service.getHighestPrice())
                .duration(service.getDuration())
                .topic(service.getTopic())
                .businessId(service.getBusiness().getId())
                .staffNames(service.getStaff().stream().map(Staff::getName).collect(Collectors.toList()))
                .build();
    }

    @PostConstruct
    private void generateDefaultSchedule() {
        List<Staff> staffList = staffRepository.findAll();
        for (Staff staff : staffList) {
            List<WorkingSchedule> schedules = new ArrayList<>();
            for (DayOfWeek day : DayOfWeek.values()) {
                schedules.add(WorkingSchedule.builder()
                        .staff(staff)
                        .dayOfWeek(day)
                        .startTime(LocalTime.of(9, 0))  // Начало рабочего дня
                        .endTime(LocalTime.of(18, 0))  // Конец рабочего дня
                        .build());
            }
            workingScheduleRepository.saveAll(schedules);
        }
    }
}
