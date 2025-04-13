package org.example.fpis_project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.ServiceCreationDto;
import org.example.fpis_project.model.dto.ServiceDto;
import org.example.fpis_project.service.impl.ServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    @PostMapping
    public ResponseEntity<?> createService(@RequestBody @Valid ServiceDto serviceDto) {
        return ResponseEntity.ok(serviceService.createService(serviceDto));
    }

    @PutMapping
    public ResponseEntity<?> updateService(@RequestBody @Valid ServiceDto serviceDto) {
        return ResponseEntity.ok(serviceService.updateService(serviceDto));
    }

    @GetMapping("/{serviceId}")
    public ServiceDto getService(@PathVariable Long serviceId) {
        return serviceService.getServiceById(serviceId);
    }

    @DeleteMapping("/{serviceId}")
    public void deleteService(@PathVariable Long serviceId) {
        serviceService.deleteService(serviceId);
    }
}
