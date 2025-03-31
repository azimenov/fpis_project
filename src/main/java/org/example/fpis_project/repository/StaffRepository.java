package org.example.fpis_project.repository;

import org.example.fpis_project.model.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Staff findByNameAndBusinessId(String serviceName, Long businessId);

    List<Staff> findByBusinessId(Long businessId);
}
