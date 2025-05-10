package org.example.fpis_project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.BusinessApplicationDto;
import org.example.fpis_project.model.dto.BusinessDto;
import org.example.fpis_project.model.entity.Business;
import org.example.fpis_project.model.entity.BusinessApplication;
import org.example.fpis_project.model.entity.Review;
import org.example.fpis_project.model.entity.Role;
import org.example.fpis_project.model.entity.User;
import org.example.fpis_project.repository.BusinessApplicationRepository;
import org.example.fpis_project.repository.BusinessRepository;
import org.example.fpis_project.repository.ReviewRepository;
import org.example.fpis_project.repository.UserRepository;
import org.example.fpis_project.service.BusinessService;
import org.example.fpis_project.util.DtoMapperUtil;
import org.example.fpis_project.util.StringListConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultBusinessService implements BusinessService {

    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final BusinessApplicationRepository businessApplicationRepository;
    private final ReviewRepository reviewRepository;
    private final StringListConverter stringListConverter = new StringListConverter();
    private final S3Service s3Service;

    @Override
    public List<BusinessDto> getAllBusinesses() {
        return businessRepository.findAll()
                .stream()
                .map(this::mapToBusinessDto).collect(Collectors.toList());
    }

    @Override
    public Optional<BusinessDto> getBusinessById(Long id) {
        return businessRepository.findById(id).map(this::mapToBusinessDto);
    }

    public Double calculateRating(Long id) {
        Business business = businessRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        List<Review> reviews = reviewRepository.findAllByBusiness(business);
        Double result = 0D;
        for(Review review : reviews) {
            result += review.getRating();
        }
        return result == 0 ? 5D : result / reviews.size();
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

        return mapToBusinessDto(businessRepository.save(business));
    }

    @Override
    public void createBusinessApplication(BusinessApplicationDto businessApplicationDto) {
        User owner = userRepository.findById(businessApplicationDto.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + businessApplicationDto.getOwnerId()));

        owner.setRole(Role.OWNER);
        userRepository.save(owner);

        BusinessApplication businessApplication = BusinessApplication.builder()
                .name(businessApplicationDto.getBusinessName())
                .city(businessApplicationDto.getCity())
                .country(businessApplicationDto.getCountry())
                .address(businessApplicationDto.getAddress())
                .phone(businessApplicationDto.getPhone())
                .description(businessApplicationDto.getDescription())
                .topic(businessApplicationDto.getBusinessType())
                .link(businessApplicationDto.getLink())
                .verified(false)
                .owner(owner)
                .createdAt(LocalDate.now())
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

        Business business = businessRepository.findById(businessDto.getId()).orElseThrow(EntityNotFoundException::new);

        List<String> currentImageUrls = stringListConverter.convertToEntityAttribute(business.getImageUrls());

        if (businessDto.getImageUrls() != null) {
            List<String> imagesToKeep = businessDto.getImageUrls();
            List<String> imagesToDelete = currentImageUrls.stream()
                    .filter(url -> !imagesToKeep.contains(url))
                    .toList();

            for (String imageUrl : imagesToDelete) {
                s3Service.deleteFile(imageUrl);
            }

            currentImageUrls = new ArrayList<>(imagesToKeep);
        }

        if (businessDto.getImages() != null && !businessDto.getImages().isEmpty()) {
            for (MultipartFile image : businessDto.getImages()) {
                if (!image.isEmpty()) {
                    String imageUrl = s3Service.uploadFile(
                            image,
                            "reviews/" + business.getId()
                    );
                    currentImageUrls.add(imageUrl);
                }
            }
        }

        business = Business.builder()
                .id(businessDto.getId())
                .name(businessDto.getName())
                .address(businessDto.getAddress())
                .phone(businessDto.getPhone())
                .description(businessDto.getDescription())
                .topic(businessDto.getTopic())
                .owner(owner)
                .build();

        return mapToBusinessDto(businessRepository.save(business));
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
        return mapToBusinessDto(businesses.get(0));
    }

    @Override
    public List<BusinessApplicationDto> getBusinessApplications() {
        return businessApplicationRepository.findAll()
                .stream()
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
                        .address(businessApplication.get().getCity() + ", " + businessApplication.get().getCountry())
                        .phone(businessApplication.get().getPhone())
                        .description(businessApplication.get().getDescription())
                        .ownerId(businessApplication.get().getOwner().getId())
                        .topic(businessApplication.get().getTopic())
                        .build()
        );
    }

    @Override
    public List<BusinessDto> searchBusiness(String searchWord) {
        return businessRepository.findByNameContaining(searchWord).stream().map(this::mapToBusinessDto).toList();
    }

    @Override
    public List<BusinessDto> getBusinessByTopic(String topic) {
        return businessRepository.findBusinessByTopic(topic).stream().map(this::mapToBusinessDto).toList();
    }

    @Override
    public BusinessApplicationDto getBusinessApplicationById(Long id) {
        Optional<BusinessApplication> businessApplication = businessApplicationRepository.findById(id);
        if (businessApplication.isEmpty()) {
            throw new EntityNotFoundException("BusinessApplication not found: " + id);
        }
        return DtoMapperUtil.mapToBusinessApplicationDto(businessApplication.get());
    }

    public BusinessDto mapToBusinessDto(Business business) {
        List<String> imageUrls = stringListConverter.convertToEntityAttribute(business.getImageUrls());

        return BusinessDto.builder()
                .id(business.getId())
                .name(business.getName())
                .address(business.getAddress())
                .phone(business.getPhone())
                .description(business.getDescription())
                .topic(business.getTopic())
                .services(
                        business.getServices() != null
                                ? business.getServices().stream().map(DtoMapperUtil::mapToServiceDto).collect(Collectors.toList())
                                : Collections.emptyList()
                )
                .ownerId(business.getOwner().getId())
                .rating(calculateRating(business.getId()))
                .imageUrls(imageUrls)
                .build();
    }

}
