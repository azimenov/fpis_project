package org.example.fpis_project.repository;

import org.example.fpis_project.model.entity.Business;
import org.example.fpis_project.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBusinessIdOrderByCreatedAtDesc(Long businessId);
    List<Review> findByCustomerEmailOrderByCreatedAtDesc(String customerEmail);

    List<Review> findAllByBusiness(Business business);
}