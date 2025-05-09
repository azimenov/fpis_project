package org.example.fpis_project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.BusinessApplicationDto;
import org.example.fpis_project.model.dto.BusinessDto;
import org.example.fpis_project.model.entity.Business;
import org.example.fpis_project.model.entity.BusinessApplication;
import org.example.fpis_project.model.entity.Role;
import org.example.fpis_project.model.entity.User;
import org.example.fpis_project.repository.BusinessApplicationRepository;
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
    private final BusinessApplicationRepository businessApplicationRepository;

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
    public void createBusinessApplication(BusinessApplicationDto businessApplicationDto) {
        User owner = userRepository.findById(businessApplicationDto.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + businessApplicationDto.getOwnerId()));

        owner.setRole(Role.OWNER);
        userRepository.save(owner);

        BusinessApplication businessApplication = BusinessApplication.builder()
                .name(businessApplicationDto.getBusinessName())
                .address(businessApplicationDto.getCity())
                .phone(businessApplicationDto.getPhone())
                .description(businessApplicationDto.getDescription())
                .topic(businessApplicationDto.getBusinessType())
                .link(businessApplicationDto.getLink())
                .verified(false)
                .build();

        businessApplicationRepository.save(businessApplication);
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
    public User getOwnerByBusinessId(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new EntityNotFoundException("Business not found: " + businessId));

        return business.getOwner();
    }

    @Override
    public BusinessDto getBusinessByUserId(Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        List<Business> businesses = businessRepository.findAllByOwner(owner);
        return DtoMapperUtil.mapToBusinessDto(businesses.get(0));
    }

    @Override
    public List<BusinessApplicationDto> getBusinessApplications() {
        return businessApplicationRepository.findAll()
                .stream()
                .filter(BusinessApplication::isVerified)
                .map(DtoMapperUtil::mapToBusinessApplicationDto)
                .toList();
    }

    @Override
    public void verifyBusinessApplication(Long businessApplicationId) {
        Optional<BusinessApplication> businessApplication = businessApplicationRepository.findById(businessApplicationId);
        if (businessApplication.isEmpty()) {
            throw new EntityNotFoundException("BusinessApplication not found: " + businessApplicationId);
        }

        businessApplication.get().setVerified(true);
        businessApplicationRepository.save(businessApplication.get());
        createBusiness(
                BusinessDto.builder()
                        .name(businessApplication.get().getName())
                        .address(businessApplication.get().getAddress())
                        .phone(businessApplication.get().getPhone())
                        .description(businessApplication.get().getDescription())
                        .ownerId(businessApplication.get().getOwner().getId())
                        .topic(businessApplication.get().getTopic())
                        .build()
        );
    }
}
