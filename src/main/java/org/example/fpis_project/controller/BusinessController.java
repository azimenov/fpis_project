package org.example.fpis_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.BusinessDto;
import org.example.fpis_project.model.dto.ServiceDto;
import org.example.fpis_project.model.dto.StaffDto;
import org.example.fpis_project.model.entity.Business;
import org.example.fpis_project.service.BusinessService;
import org.example.fpis_project.service.impl.ServiceService;
import org.example.fpis_project.service.impl.StaffService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;
    private final ServiceService serviceService;
    private final StaffService staffService;

    @GetMapping
    public List<BusinessDto> getAllBusinesses() {
        return businessService.getAllBusinesses();
    }

    @GetMapping("/{id}")
    public Optional<BusinessDto> getBusinessById(@PathVariable Long id) {
        return businessService.getBusinessById(id);
    }

    @PostMapping
    public BusinessDto createBusiness(@RequestBody Business business) {
        return businessService.createBusiness(business);
    }

    @DeleteMapping("/{id}")
    public void deleteBusiness(@PathVariable Long id) {
        businessService.deleteBusiness(id);
    }

    @GetMapping("/services/{businessId}")
    public List<ServiceDto> getServicesById(@PathVariable Long businessId) {
        return serviceService.getAllServices(businessId);
    }

    @GetMapping("/staff/{businessId}")
    public List<StaffDto> getStaffById(@PathVariable Long businessId) {
        return staffService.getStaffByBusinessId(businessId);
    }
}
