package org.example.fpis_project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.Reservation;
import org.example.fpis_project.model.dto.ReviewDto;
import org.example.fpis_project.model.entity.Business;
import org.example.fpis_project.model.entity.Review;
import org.example.fpis_project.repository.BusinessRepository;
import org.example.fpis_project.repository.ReservationRepository;
import org.example.fpis_project.repository.ReviewRepository;
import org.example.fpis_project.util.StringListConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BusinessRepository businessRepository;
    private final ReservationRepository reservationRepository;
    private final S3Service s3Service;
    private final StringListConverter stringListConverter = new StringListConverter();

    public ReviewDto createReview(ReviewDto reviewDto) {
        Business business = businessRepository.findById(reviewDto.getBusinessId())
                .orElseThrow(() -> new EntityNotFoundException("Business not found: " + reviewDto.getBusinessId()));

        Review review = Review.builder()
                .business(business)
                .customerName(reviewDto.getCustomerName())
                .customerEmail(reviewDto.getCustomerEmail())
                .rating(reviewDto.getRating())
                .comment(reviewDto.getComment())
                .createdAt(LocalDateTime.now())
                .isVerified(false)
                .build();

        if (reviewDto.getReservationId() != null) {
            Reservation reservation = reservationRepository.findById(reviewDto.getReservationId())
                    .orElseThrow(() -> new EntityNotFoundException("Reservation not found: " + reviewDto.getReservationId()));

            if (!reservation.getCustomerEmail().equals(reviewDto.getCustomerEmail())) {
                throw new IllegalArgumentException("Reservation does not belong to this customer");
            }

            if (!reservation.getBusiness().getId().equals(reviewDto.getBusinessId())) {
                throw new IllegalArgumentException("Reservation does not belong to this business");
            }

            review.setReservation(reservation);
        }

        List<String> imageUrls = new ArrayList<>();
        if (reviewDto.getImages() != null && !reviewDto.getImages().isEmpty()) {
            for (MultipartFile image : reviewDto.getImages()) {
                if (!image.isEmpty()) {
                    String imageUrl = s3Service.uploadFile(
                            image,
                            "reviews/" + business.getId()
                    );
                    imageUrls.add(imageUrl);
                }
            }
        }

        review.setImageUrls(stringListConverter.convertToDatabaseColumn(imageUrls));

        Review savedReview = reviewRepository.save(review);
        return convertToDto(savedReview);
    }

    public List<ReviewDto> getBusinessReviews(Long businessId) {
        if (!businessRepository.existsById(businessId)) {
            throw new EntityNotFoundException("Business not found: " + businessId);
        }

        List<Review> reviews = reviewRepository.findByBusinessIdOrderByCreatedAtDesc(businessId);
        return reviews.stream()
                .filter(Review::isVerified)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ReviewDto> getCustomerReviews(String email) {
        List<Review> reviews = reviewRepository.findByCustomerEmailOrderByCreatedAtDesc(email);
        return reviews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDto updateReview(Long reviewId, ReviewDto reviewDto) {
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found: " + reviewId));

        if (!existingReview.getCustomerEmail().equals(reviewDto.getCustomerEmail())) {
            throw new IllegalArgumentException("Review does not belong to this customer");
        }

        existingReview.setRating(reviewDto.getRating());
        existingReview.setComment(reviewDto.getComment());
        List<String> currentImageUrls = stringListConverter.convertToEntityAttribute(existingReview.getImageUrls());

        if (reviewDto.getImageUrls() != null) {
            List<String> imagesToKeep = reviewDto.getImageUrls();
            List<String> imagesToDelete = currentImageUrls.stream()
                    .filter(url -> !imagesToKeep.contains(url))
                    .toList();

            for (String imageUrl : imagesToDelete) {
                s3Service.deleteFile(imageUrl);
            }

            currentImageUrls = new ArrayList<>(imagesToKeep);
        }

        if (reviewDto.getImages() != null && !reviewDto.getImages().isEmpty()) {
            for (MultipartFile image : reviewDto.getImages()) {
                if (!image.isEmpty()) {
                    String imageUrl = s3Service.uploadFile(
                            image,
                            "reviews/" + existingReview.getBusiness().getId()
                    );
                    currentImageUrls.add(imageUrl);
                }
            }
        }

        // Update image URLs
        existingReview.setImageUrls(stringListConverter.convertToDatabaseColumn(currentImageUrls));

        Review updatedReview = reviewRepository.save(existingReview);
        return convertToDto(updatedReview);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found: " + reviewId));

        List<String> imageUrls = stringListConverter.convertToEntityAttribute(review.getImageUrls());
        for (String imageUrl : imageUrls) {
            s3Service.deleteFile(imageUrl);
        }

        reviewRepository.delete(review);
    }

    public Map<String, Object> getBusinessRatingStats(Long businessId) {
        if (!businessRepository.existsById(businessId)) {
            throw new EntityNotFoundException("Business not found: " + businessId);
        }

        List<Review> reviews = reviewRepository.findByBusinessIdOrderByCreatedAtDesc(businessId);

        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(5.0);

        Map<Integer, Long> ratingDistribution = reviews.stream()
                .collect(Collectors.groupingBy(Review::getRating, Collectors.counting()));

        for (int i = 1; i <= 5; i++) {
            ratingDistribution.putIfAbsent(i, 0L);
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("averageRating", Math.round(averageRating * 10.0) / 10.0); // Round to 1 decimal place
        stats.put("totalReviews", reviews.size());
        stats.put("ratingDistribution", ratingDistribution);

        return stats;
    }

    public void verifyReview(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if (review.isEmpty()) {
            throw new EntityNotFoundException("Review not found: " + reviewId);
        }

        review.get().setVerified(true);
        reviewRepository.save(review.get());
    }

    private ReviewDto convertToDto(Review review) {
        List<String> imageUrls = stringListConverter.convertToEntityAttribute(review.getImageUrls());

        return ReviewDto.builder()
                .id(review.getId())
                .businessId(review.getBusiness().getId())
                .reservationId(review.getReservation() != null ? review.getReservation().getId() : null)
                .customerName(review.getCustomerName())
                .customerEmail(review.getCustomerEmail())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .isVerified(review.isVerified())
                .imageUrls(imageUrls)
                .build();
    }

    public List<ReviewDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}