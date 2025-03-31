package org.example.fpis_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.entity.Staff;
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
    public List<Staff> getServices(
            @PathVariable("businessId") Long businessId
    ) {
        return service.getServices(businessId);
    }
}
