package org.example.fpis_project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.ReviewDto;
import org.example.fpis_project.service.impl.ReviewImageService;
import org.example.fpis_project.service.impl.ReviewService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;

    @PostMapping(value = "/{reviewId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReviewDto> addImagesToReviewApplication(
            @PathVariable Long reviewId,
            @RequestParam("images") List<MultipartFile> images) throws IOException {

        ReviewDto updatedReview = reviewImageService.addImagesToReview(reviewId, images);
        return ResponseEntity.ok(updatedReview);
    }

    @GetMapping("/images/{imageId}")
    public ResponseEntity<byte[]> getReviewImage(@PathVariable Long imageId) {
        var imageData = reviewImageService.getReviewImage(imageId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(imageData.getContentType()));

        return new ResponseEntity<>(imageData.getImageData(), headers, HttpStatus.OK);
    }

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
            @PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}