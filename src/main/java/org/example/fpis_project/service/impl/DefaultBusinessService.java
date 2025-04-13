package org.example.fpis_project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.BusinessDto;
import org.example.fpis_project.model.entity.Business;
import org.example.fpis_project.model.entity.Role;
import org.example.fpis_project.model.entity.User;
import org.example.fpis_project.repository.BusinessRepository;
import org.example.fpis_project.repository.UserRepository;
import org.example.fpis_project.service.BusinessService;
import org.example.fpis_project.util.DtoMapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultBusinessService implements BusinessService {

    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;

    @Override
    public List<BusinessDto> getAllBusinesses() {
        return businessRepository.findAll()
                .stream()
                .map(DtoMapperUtil::mapToBusinessDto).collect(Collectors.toList());
    }

    @Override
    public Optional<BusinessDto> getBusinessById(Long id) {
        return businessRepository.findById(id).map(DtoMapperUtil::mapToBusinessDto);
    }

    @Override
    public BusinessDto createBusiness(BusinessDto businessDto) {
        User owner = userRepository.findById(businessDto.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + businessDto.getOwnerId()));

        owner.setRole(Role.OWNER);
        userRepository.save(owner);

        Business business = Business.builder()
                .name(businessDto.getName())
                .address(businessDto.getAddress())
                .phone(businessDto.getPhone())
                .description(businessDto.getDescription())
                .topic(businessDto.getTopic())
                .owner(owner)
                .build();

        return DtoMapperUtil.mapToBusinessDto(businessRepository.save(business));
    }


    @Override
    public void deleteBusiness(Long id) {
        businessRepository.deleteById(id);
    }

    @Override
    public BusinessDto updateBusiness(BusinessDto businessDto) {
        User owner = userRepository.findById(businessDto.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + businessDto.getOwnerId()));

        Business business = Business.builder()
                .id(businessDto.getId())
                .name(businessDto.getName())
                .address(businessDto.getAddress())
                .phone(businessDto.getPhone())
                .description(businessDto.getDescription())
                .topic(businessDto.getTopic())
                .owner(owner)
                .build();

        return DtoMapperUtil.mapToBusinessDto(businessRepository.save(business));
    }

    @Override
    public User getOwner(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new EntityNotFoundException("Business not found: " + businessId));

        return business.getOwner();
    }
}
