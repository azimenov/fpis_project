package org.example.fpis_project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.ServiceDto;
import org.example.fpis_project.model.entity.Business;
import org.example.fpis_project.model.entity.Service;
import org.example.fpis_project.model.entity.Staff;
import org.example.fpis_project.repository.ServiceRepository;
import org.example.fpis_project.repository.StaffRepository;
import org.example.fpis_project.util.DtoMapperUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final StaffRepository staffRepository;
    private final BusinessRepository businessRepository;

    public List<ServiceDto> getAllServices(Long businessId) {
        return serviceRepository.findAllByBusinessId(businessId).stream().map(
                DtoMapperUtil::mapToServiceDto
        ).collect(Collectors.toList());
    }

    public ServiceDto getServiceById(Long serviceId) {
        return DtoMapperUtil.mapToServiceDto(serviceRepository.findServiceById(serviceId));
    }

    public ServiceDto createService(ServiceDto serviceDto) {
        Business business = businessRepository.findById(serviceDto.getBusinessId())
                .orElseThrow(() -> new EntityNotFoundException("Business not found with ID: " + serviceDto.getBusinessId()));

        List<Staff> staffList = new ArrayList<>();

        for (Long staffId : serviceDto.getStaffIds()) {
            Staff staff = staffRepository.findById(staffId)
                    .orElseThrow(() -> new EntityNotFoundException("Staff not found with ID: " + staffId));

            staffList.add(staff);
        }

        Service newService = Service
                .builder()
                .name(serviceDto.getName())
                .lowestPrice(serviceDto.getLowestPrice())
                .highestPrice(serviceDto.getHighestPrice())
                .duration(serviceDto.getDuration())
                .topic(serviceDto.getTopic())
                .business(business)
                .staff(staffList)
                .build();

        return DtoMapperUtil.mapToServiceDto(serviceRepository.save(newService));
    }

    public ServiceDto updateService(ServiceDto serviceDto) {
        Service existingService = serviceRepository.findById(serviceDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Service not found with ID: " + serviceDto.getId()));

        Business business = null;
        if (!existingService.getBusiness().getId().equals(serviceDto.getBusinessId())) {
            business = businessRepository.findById(serviceDto.getBusinessId())
                    .orElseThrow(() -> new EntityNotFoundException("Business not found with ID: " + serviceDto.getBusinessId()));
        } else {
            business = existingService.getBusiness();
        }

        List<Staff> staffList = new ArrayList<>();
        for (Long staffId : serviceDto.getStaffIds()) {
            Staff staff = staffRepository.findById(staffId)
                    .orElseThrow(() -> new EntityNotFoundException("Staff not found with ID: " + staffId));
            staffList.add(staff);
        }
        existingService.setId(serviceDto.getId());
        existingService.setName(serviceDto.getName());
        existingService.setLowestPrice(serviceDto.getLowestPrice());
        existingService.setHighestPrice(serviceDto.getHighestPrice());
        existingService.setDuration(serviceDto.getDuration());
        existingService.setTopic(serviceDto.getTopic());
        existingService.setBusiness(business);
        existingService.setStaff(staffList);

        return DtoMapperUtil.mapToServiceDto(serviceRepository.save(existingService));
    }

    public void deleteService(Long serviceId) {
        serviceRepository.deleteById(serviceId);
    }
}
