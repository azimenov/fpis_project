package org.example.fpis_project.util;

import org.example.fpis_project.model.dto.BusinessApplicationDto;
import org.example.fpis_project.model.dto.BusinessDto;
import org.example.fpis_project.model.dto.ServiceDto;
import org.example.fpis_project.model.dto.StaffDto;
import org.example.fpis_project.model.entity.Business;
import org.example.fpis_project.model.entity.BusinessApplication;
import org.example.fpis_project.model.entity.Staff;

import java.util.Collections;
import java.util.stream.Collectors;

public class DtoMapperUtil {

    public static ServiceDto mapToServiceDto(org.example.fpis_project.model.entity.Service service) {
        return ServiceDto.builder()
                .id(service.getId())
                .name(service.getName())
                .lowestPrice(service.getLowestPrice())
                .highestPrice(service.getHighestPrice())
                .duration(service.getDuration())
                .topic(service.getTopic())
                .businessId(service.getBusiness().getId())
                .staffIds(service.getStaff().stream().map(Staff::getId).collect(Collectors.toList()))
                .build();
    }

    public static StaffDto mapToStaffDto(Staff staff) {
        return StaffDto.builder()
                .id(staff.getId())
                .name(staff.getName())
                .surname(staff.getSurname())
                .phone(staff.getPhone())
                .position(staff.getPosition())
                .businessId(staff.getBusiness().getId())
                .services(
                        staff.getServices() != null
                                ? staff.getServices().stream().map(DtoMapperUtil::mapToServiceDto).collect(Collectors.toList())
                                : Collections.emptyList()
                )
                .build();
    }

    public static BusinessApplicationDto mapToBusinessApplicationDto(BusinessApplication businessApplication) {
        return BusinessApplicationDto.builder()
                .id(businessApplication.getId())
                .ownerName(businessApplication.getOwner().getFullname())
                .ownerId(businessApplication.getOwner().getId())
                .phone(businessApplication.getPhone())
                .country(businessApplication.getCountry())
                .city(businessApplication.getCity())
                .businessName(businessApplication.getName())
                .businessType(businessApplication.getTopic())
                .link(businessApplication.getLink())
                .description(businessApplication.getDescription())
                .createdAt(businessApplication.getCreatedAt())
                .verified(businessApplication.isVerified())
                .build();
    }
}
