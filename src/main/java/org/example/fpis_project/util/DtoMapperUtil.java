package org.example.fpis_project.util;

import org.example.fpis_project.model.dto.BusinessDto;
import org.example.fpis_project.model.dto.ServiceDto;
import org.example.fpis_project.model.dto.StaffDto;
import org.example.fpis_project.model.entity.Business;
import org.example.fpis_project.model.entity.Staff;

import java.util.stream.Collectors;

public class DtoMapperUtil {

    public static BusinessDto mapToBusinessDto(Business business) {
        return BusinessDto.builder()
                .id(business.getId())
                .name(business.getName())
                .address(business.getAddress())
                .phone(business.getPhone())
                .services(
                        business.getServices().stream().map(DtoMapperUtil::mapToServiceDto).collect(Collectors.toList())
                )
                .build();
    }

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
                .position(staff.getPosition())
                .businessId(staff.getBusiness().getId())
                .services(staff.getServices().stream().map(DtoMapperUtil::mapToServiceDto).collect(Collectors.toList()))
                .build();
    }
}
