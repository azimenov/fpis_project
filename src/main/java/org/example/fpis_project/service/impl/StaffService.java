package org.example.fpis_project.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.StaffDto;
import org.example.fpis_project.model.entity.Staff;
import org.example.fpis_project.model.entity.WorkingSchedule;
import org.example.fpis_project.repository.StaffRepository;
import org.example.fpis_project.repository.WorkingScheduleRepository;
import org.example.fpis_project.util.DtoMapperUtil;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository repository;
    private final WorkingScheduleRepository workingScheduleRepository;
    private final StaffRepository staffRepository;

    public List<StaffDto> getStaffByBusinessId(Long businessId) {
        return repository.findByBusinessId(businessId).stream()
                .map(DtoMapperUtil::mapToStaffDto)
                .collect(Collectors.toList());
    }


    public StaffDto getStaff(Long id) {
        return DtoMapperUtil.mapToStaffDto(Objects.requireNonNull(staffRepository.findById(id).orElse(null)));
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
