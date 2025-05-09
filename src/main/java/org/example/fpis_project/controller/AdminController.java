package org.example.fpis_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.BusinessApplicationDto;
import org.example.fpis_project.model.dto.ReviewDto;
import org.example.fpis_project.service.BusinessService;
import org.example.fpis_project.service.impl.ReviewService;
import org.example.fpis_project.service.impl.ServiceService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final ReviewService reviewService;
    private final ServiceService serviceService;
    private final BusinessService businessService;

    @PatchMapping("/review/verify")
    public void verifyReview(
            @RequestParam Long reviewId
    ) {
        reviewService.verifyReview(reviewId);
    }

    @GetMapping("/review/{businessId}")
    public List<ReviewDto> getReviews(@PathVariable Long businessId) {
        return reviewService.getBusinessReviews(businessId);
    }

    @DeleteMapping("/service/{serviceId}")
    public void deleteService(@PathVariable Long serviceId) {
        serviceService.deleteService(serviceId);
    }

    @GetMapping("/businessApplications")
    public List<BusinessApplicationDto> getBusinessApplications() {
        return businessService.getBusinessApplications();
    }

    @PutMapping("/businessApplications/{businessApplicationId}")
    public void updateBusinessApplication(
            @PathVariable Long businessApplicationId
    ) {
        businessService.verifyBusinessApplication(businessApplicationId);
    }
}
