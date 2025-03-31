package org.example.fpis_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.BusinessDto;
import org.example.fpis_project.model.dto.ServiceDto;
import org.example.fpis_project.model.entity.Business;
import org.example.fpis_project.model.entity.Staff;
import org.example.fpis_project.repository.BusinessRepository;
import org.example.fpis_project.service.BusinessService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultBusinessService implements BusinessService {

    private final BusinessRepository businessRepository;

    @Override
    public List<BusinessDto> getAllBusinesses() {
        return businessRepository.findAll()
                .stream()
                .map(this::mapToBusinessDto).collect(Collectors.toList());
    }

    private BusinessDto mapToBusinessDto(Business business) {
        return BusinessDto.builder()
                .id(business.getId())
                .name(business.getName())
                .address(business.getAddress())
                .phone(business.getPhone())
                .services(
                        business.getServices().stream().map(this::mapToServiceDto).collect(Collectors.toList())
                )
                .build();
    }

    private ServiceDto mapToServiceDto(org.example.fpis_project.model.entity.Service service) {
        return ServiceDto.builder()
                .name(service.getName())
                .lowestPrice(service.getLowestPrice())
                .highestPrice(service.getHighestPrice())
                .duration(service.getDuration())
                .topic(service.getTopic())
                .businessId(service.getBusiness().getId())
                .staffNames(service.getStaff().stream().map(Staff::getName).collect(Collectors.toList()))
                .build();
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
