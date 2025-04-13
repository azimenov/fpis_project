package org.example.fpis_project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.ReviewDto;
import org.example.fpis_project.service.impl.ReviewService;
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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@Valid @RequestBody ReviewDto reviewDto) {
        return ResponseEntity.ok(reviewService.createReview(reviewDto));
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<ReviewDto>> getBusinessReviews(@PathVariable Long businessId) {
        return ResponseEntity.ok(reviewService.getBusinessReviews(businessId));
    }

    @GetMapping("/customer")
    public ResponseEntity<List<ReviewDto>> getCustomerReviews(@RequestParam String email) {
        return ResponseEntity.ok(reviewService.getCustomerReviews(email));
    }

    @GetMapping("/business/{businessId}/stats")
    public ResponseEntity<Map<String, Object>> getBusinessRatingStats(@PathVariable Long businessId) {
        return ResponseEntity.ok(reviewService.getBusinessRatingStats(businessId));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewDto reviewDto) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, reviewDto));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @RequestParam String customerEmail) {
        reviewService.deleteReview(reviewId, customerEmail);
        return ResponseEntity.noContent().build();
    }
}