package org.example.fpis_project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.BusinessApplicationDto;
import org.example.fpis_project.model.dto.BusinessDto;
import org.example.fpis_project.model.dto.BusinessImageDto;
import org.example.fpis_project.model.dto.ServiceDto;
import org.example.fpis_project.model.dto.StaffDto;
import org.example.fpis_project.model.entity.User;
import org.example.fpis_project.service.BusinessService;
import org.example.fpis_project.service.impl.BusinessImageService;
import org.example.fpis_project.service.impl.ServiceService;
import org.example.fpis_project.service.impl.StaffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;
    private final ServiceService serviceService;
    private final StaffService staffService;
    private final BusinessImageService businessImageService;

    @PostMapping("/images/{businessApplicationId}")
    public ResponseEntity<BusinessApplicationDto> addImagesToBusinessApplication(
            @PathVariable Long businessApplicationId,
            @RequestParam("images") List<MultipartFile> images) throws IOException {

        BusinessApplicationDto updatedBusinessApplication =
                businessImageService.addImagesToBusinessApplication(
                        businessApplicationId, images);
        return ResponseEntity.ok(updatedBusinessApplication);
    }

    @GetMapping("/images/{businessId}")
    public ResponseEntity<List<BusinessImageDto>> getBusinessImages(@PathVariable Long businessId) {
        List<BusinessImageDto> images = businessImageService.getBusinessImages(businessId);
        return ResponseEntity.ok(images);
    }

    @GetMapping("/images/{businessApplicationId}")
    public ResponseEntity<List<BusinessImageDto>> getBusinessApplicationImages(
            @PathVariable Long businessApplicationId) {
        List<BusinessImageDto> images =
                businessImageService.getBusinessApplicationImages(businessApplicationId);
        return ResponseEntity.ok(images);
    }

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

    @GetMapping("/search/{searchWord}")
    public List<BusinessDto> searchBusiness(@PathVariable String searchWord) {
        return businessService.searchBusiness(searchWord);
    }

    @GetMapping("/topic/{topic}")
    public List<BusinessDto> getBusinessByTopic(@PathVariable String topic) {
        return businessService.getBusinessByTopic(topic);
    }
}
