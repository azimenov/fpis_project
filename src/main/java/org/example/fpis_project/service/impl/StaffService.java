package org.example.fpis_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.entity.Staff;
import org.example.fpis_project.model.entity.WorkingSchedule;
import org.example.fpis_project.repository.StaffRepository;
import org.example.fpis_project.repository.WorkingScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository repository;
    private final WorkingScheduleRepository workingScheduleRepository;

    public List<Staff> getServices(Long businessId) {
        return repository.findByBusinessId(businessId);
    }

    private void generateDefaultSchedule(Staff staff) {
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
