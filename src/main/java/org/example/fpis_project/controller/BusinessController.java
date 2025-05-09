package org.example.fpis_project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.BusinessApplicationDto;
import org.example.fpis_project.model.dto.BusinessDto;
import org.example.fpis_project.model.dto.ServiceDto;
import org.example.fpis_project.model.dto.StaffDto;
import org.example.fpis_project.model.entity.User;
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

    @PostMapping
    public void createBusiness(@RequestBody @Valid BusinessApplicationDto business) {
        businessService.createBusinessApplication(business);
    }

    @GetMapping
    public List<BusinessDto> getAllBusinesses() {
        return businessService.getAllBusinesses();
    }

    @PutMapping
    public BusinessDto updateBusiness(@RequestBody @Valid BusinessDto business) {
        return businessService.updateBusiness(business);
    }

    @GetMapping("/{id}")
    public Optional<BusinessDto> getBusinessById(@PathVariable Long id) {
        return businessService.getBusinessById(id);
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

    @GetMapping("/owner")
    public User getOwner(
            @RequestParam Long businessId
    ) {
        return businessService.getOwnerByBusinessId(businessId);
    }

    @GetMapping("/owner/{userId}")
    public BusinessDto getBusinessByUserId(@PathVariable Long userId) {
        return businessService.getBusinessByUserId(userId);
    }
}
