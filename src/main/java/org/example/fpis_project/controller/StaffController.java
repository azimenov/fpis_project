package org.example.fpis_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.StaffDto;
import org.example.fpis_project.service.impl.StaffService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService service;

    @GetMapping("/{businessId}")
    public List<StaffDto> getServices(
            @PathVariable("businessId") Long businessId
    ) {
        return service.getServices(businessId);
    }

    @GetMapping("/{staffId}")
    public StaffDto getStaff(
            @PathVariable Long staffId
    ) {
        return service.getStaff(staffId);
    }
}
