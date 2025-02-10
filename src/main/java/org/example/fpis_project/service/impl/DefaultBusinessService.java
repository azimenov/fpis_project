package org.example.fpis_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.fpis_project.entity.Business;
import org.example.fpis_project.repository.BusinessRepository;
import org.example.fpis_project.service.BusinessService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultBusinessService implements BusinessService {

    private final BusinessRepository businessRepository;

    @Override
    public List<Business> getAllBusinesses() {
        return businessRepository.findAll();
    }

    @Override
    public Optional<Business> getBusinessById(Long id) {
        return businessRepository.findById(id);
    }

    @Override
    public Business createBusiness(Business business) {
        return businessRepository.save(business);
    }

    @Override
    public void deleteBusiness(Long id) {
        businessRepository.deleteById(id);
    }
}
