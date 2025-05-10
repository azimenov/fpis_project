package org.example.fpis_project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.BusinessApplicationDto;
import org.example.fpis_project.model.dto.BusinessImageDto;
import org.example.fpis_project.model.entity.BusinessApplication;
import org.example.fpis_project.model.entity.BusinessImage;
import org.example.fpis_project.repository.BusinessApplicationRepository;
import org.example.fpis_project.repository.BusinessImageRepository;
import org.example.fpis_project.service.BusinessService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusinessImageService {
    private final BusinessImageRepository businessImageRepository;
    private final BusinessService businessService;
    private final BusinessApplicationRepository businessApplicationRepository;

    public BusinessApplicationDto addImagesToBusinessApplication(Long businessApplicationId, List<MultipartFile> images) throws IOException {
        BusinessApplication business = businessApplicationRepository.findById(businessApplicationId)
                .orElseThrow(() -> new EntityNotFoundException("Business not found: " + businessApplicationId));

        for (MultipartFile image : images) {
            BusinessImage businessImage = BusinessImage.builder()
                    .businessApplication(business)
                    .imageData(image.getBytes())
                    .contentType(image.getContentType())
                    .fileName(image.getOriginalFilename())
                    .build();

            business.getImages().add(businessImage);
            businessImageRepository.save(businessImage);
        }

        businessApplicationRepository.save(business);
        return businessService.getBusinessApplicationById(businessApplicationId);
    }

    public List<BusinessImageDto> getBusinessImages(Long businessId) {
        return businessImageRepository.findByBusinessId(businessId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<BusinessImageDto> getBusinessApplicationImages(Long businessApplicationId) {
        return businessImageRepository.findByBusinessApplicationId(businessApplicationId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public BusinessImageDto convertToDto(BusinessImage businessImage) {
        String base64Image = Base64.getEncoder().encodeToString(businessImage.getImageData());
        String dataUrl = "data:" + businessImage.getContentType() + ";base64," + base64Image;

        return BusinessImageDto.builder()
                .id(businessImage.getId())
                .businessId(businessImage.getBusiness() != null ? businessImage.getBusiness().getId() : null)
                .businessApplicationId(businessImage.getBusinessApplication() != null ?
                        businessImage.getBusinessApplication().getId() : null)
                .contentType(businessImage.getContentType())
                .fileName(businessImage.getFileName())
                .dataUrl(dataUrl)
                .build();
    }

}