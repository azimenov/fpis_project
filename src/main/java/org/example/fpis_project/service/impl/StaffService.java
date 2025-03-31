package org.example.fpis_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.entity.Staff;
import org.example.fpis_project.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository repository;

    public List<Staff> getServices(Long businessId) {
        return repository.findByBusinessId(businessId);
    }
}
