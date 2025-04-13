package org.example.fpis_project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.StaffDto;
import org.example.fpis_project.service.impl.StaffService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService service;

    @GetMapping("/{staffId}")
    public StaffDto getStaff(
            @PathVariable Long staffId
    ) {
        return service.getStaff(staffId);
    }

    @PostMapping
    public StaffDto createStaff(@RequestBody @Valid StaffDto staffDto) {
        return service.createStaff(staffDto);
    }
}
