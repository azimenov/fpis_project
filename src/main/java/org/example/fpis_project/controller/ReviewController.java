package org.example.fpis_project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.ReviewDto;
import org.example.fpis_project.service.impl.ReviewService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReviewDto> createReview(
            @RequestPart("review") @Valid ReviewDto reviewDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        reviewDto.setImages(images);
        return ResponseEntity.ok(reviewService.createReview(reviewDto));
    }

    @PutMapping(value = "/{reviewId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable Long reviewId,
            @RequestPart("review") @Valid ReviewDto reviewDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        reviewDto.setImages(images);
        return ResponseEntity.ok(reviewService.updateReview(reviewId, reviewDto));
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

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{reviewId}/verify")
    public ResponseEntity<Void> verifyReview(@PathVariable Long reviewId) {
        reviewService.verifyReview(reviewId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }
}