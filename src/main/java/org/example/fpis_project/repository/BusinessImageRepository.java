package org.example.fpis_project.repository;

import org.example.fpis_project.model.entity.BusinessApplication;
import org.example.fpis_project.model.entity.BusinessImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface BusinessImageRepository extends JpaRepository<BusinessImage, Long> {
    List<BusinessImage> findByBusinessId(Long businessId);

    List<BusinessImage> findByBusinessApplication(BusinessApplication businessApplication);

    List<BusinessImage> findByBusinessApplicationId(Long businessApplicationId);

}