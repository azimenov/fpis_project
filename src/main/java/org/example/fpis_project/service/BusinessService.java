package org.example.fpis_project.service;

import org.example.fpis_project.model.dto.BusinessDto;
import org.example.fpis_project.model.entity.Business;

import java.util.List;
import java.util.Optional;

public interface BusinessService {
    List<BusinessDto> getAllBusinesses();

    Optional<BusinessDto> getBusinessById(Long id);

    BusinessDto createBusiness(Business business);

    void deleteBusiness(Long id);
}
