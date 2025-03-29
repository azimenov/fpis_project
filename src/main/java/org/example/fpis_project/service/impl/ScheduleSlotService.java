package org.example.fpis_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.entity.ScheduleSlot;
import org.example.fpis_project.repository.ScheduleSlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleSlotService {

    private final ScheduleSlotRepository slotRepository;

    public List<ScheduleSlot> findAvailableSlots(Long staffId) {
        return slotRepository.findByStaffIdAndScheduledFalse(staffId);
    }

}
