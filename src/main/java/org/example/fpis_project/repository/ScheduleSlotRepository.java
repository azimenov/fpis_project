package org.example.fpis_project.repository;

import org.example.fpis_project.model.entity.ScheduleSlot;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScheduleSlotRepository extends CrudRepository<ScheduleSlot, Long> {

    List<ScheduleSlot> findByStaffIdAndScheduledFalse(Long staffId);

}
