package org.example.fpis_project.repository;

import org.example.fpis_project.model.entity.WorkingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkingScheduleRepository extends JpaRepository<WorkingSchedule, Long> {
    List<WorkingSchedule> findAllByStaffId(Long staffId);

}
