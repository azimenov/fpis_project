package org.example.fpis_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.ServiceDto;
import org.example.fpis_project.model.entity.Service;
import org.example.fpis_project.service.impl.ServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    @GetMapping("/{serviceId}")
    public ServiceDto getService(@PathVariable Long serviceId) {
        return serviceService.getServiceById(serviceId);
    }

    @PostMapping
    public ResponseEntity<?> createService(@RequestBody ServiceDto serviceDto) {
        serviceService.createService(serviceDto);

        return ResponseEntity.ok().build();
    }
}
