package org.example.fpis_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.ServiceDto;
import org.example.fpis_project.model.entity.Business;
import org.example.fpis_project.model.entity.Service;
import org.example.fpis_project.model.entity.Staff;
import org.example.fpis_project.repository.BusinessRepository;
import org.example.fpis_project.repository.ServiceRepository;
import org.example.fpis_project.repository.StaffRepository;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final StaffRepository staffRepository;
    private final BusinessRepository businessRepository;

    public List<Service> getAllServices(Long businessId) {
        return serviceRepository.findAllByBusinessId(businessId);
    }

    public void createService(ServiceDto serviceDto) {

        Staff staff = staffRepository.findByNameAndBusinessId(
                serviceDto.getName(),
                serviceDto.getBusinessId()
        );

        Business business = businessRepository.findById(serviceDto.getBusinessId()).get();

        Service newService = Service
                .builder()
                .name(serviceDto.getName())
                .lowestPrice(serviceDto.getLowestPrice())
                .highestPrice(serviceDto.getHighestPrice())
                .duration(serviceDto.getDuration())
                .topic(serviceDto.getTopic())
                .staff(List.of(staff))
                .business(business)
                .build();

        serviceRepository.save(newService);
    }
}
