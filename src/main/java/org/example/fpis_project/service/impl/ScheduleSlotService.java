package org.example.fpis_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.entity.ScheduleSlot;
import org.example.fpis_project.model.entity.Staff;
import org.example.fpis_project.model.entity.WorkingSchedule;
import org.example.fpis_project.repository.ScheduleSlotRepository;
import org.example.fpis_project.repository.WorkingScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleSlotService {

    private final ScheduleSlotRepository slotRepository;
    private final WorkingScheduleRepository workingScheduleRepository;

    public List<ScheduleSlot> findAvailableSlots(Long staffId) {
        List<WorkingSchedule> workingSchedules = workingScheduleRepository.findAllByStaffId(staffId);
        List<ScheduleSlot> bookedSlots = slotRepository.findByStaffIdAndScheduled(staffId, true);

        List<ScheduleSlot> availableSlots = new ArrayList<>();

        for (WorkingSchedule schedule : workingSchedules) {
            LocalDateTime currentStart = LocalDateTime.now()
                    .with(TemporalAdjusters.nextOrSame(schedule.getDayOfWeek()))
                    .with(schedule.getStartTime());

            LocalDateTime endTime = currentStart.with(schedule.getEndTime());

            while (currentStart.plusMinutes(60).isBefore(endTime)) {
                LocalDateTime potentialEnd = currentStart.plusMinutes(60);

                LocalDateTime finalCurrentStart = currentStart;
                boolean isOverlapping = bookedSlots.stream().anyMatch(slot ->
                        slot.getStartTime().isBefore(potentialEnd) && slot.getEndTime().isAfter(finalCurrentStart));

                if (!isOverlapping) {
                    availableSlots.add(ScheduleSlot.builder()
                            .staff(Staff.builder()
                                    .id(staffId)
                                    .build())
                            .startTime(currentStart)
                            .endTime(potentialEnd)
                            .scheduled(false)
                            .build());
                }

                currentStart = currentStart.plusMinutes(60);
            }
        }
        return availableSlots;
    }
}
