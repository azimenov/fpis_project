package org.example.fpis_project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.ReviewDto;
import org.example.fpis_project.model.entity.Review;
import org.example.fpis_project.model.entity.ReviewImage;
import org.example.fpis_project.repository.ReviewImageRepository;
import org.example.fpis_project.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewImageService {
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewService reviewService;

    public ReviewDto addImagesToReview(Long reviewId, List<MultipartFile> images) throws IOException {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found: " + reviewId));

        for (MultipartFile image : images) {
            ReviewImage reviewImage = ReviewImage.builder()
                    .review(review)
                    .imageData(image.getBytes())
                    .contentType(image.getContentType())
                    .fileName(image.getOriginalFilename())
                    .build();

            review.getImages().add(reviewImage);
        }

        Review savedReview = reviewRepository.save(review);
        return reviewService.convertToDto(savedReview);
    }

    public ReviewImage getReviewImage(Long imageId) {
        return reviewImageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Image not found: " + imageId));
    }

    public void deleteReviewImage(Long imageId) {
        ReviewImage image = reviewImageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Image not found: " + imageId));

        reviewImageRepository.delete(image);
    }

}