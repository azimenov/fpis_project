package org.example.fpis_project.service;

import org.example.fpis_project.model.dto.BusinessDto;
import org.example.fpis_project.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface BusinessService {
    List<BusinessDto> getAllBusinesses();

    Optional<BusinessDto> getBusinessById(Long id);

    BusinessDto createBusiness(BusinessDto business);

    void deleteBusiness(Long id);

    BusinessDto updateBusiness(BusinessDto business);

    User getOwnerByBusinessId(Long businessId);

    BusinessDto getBusinessByUserId(Long userId);
}
