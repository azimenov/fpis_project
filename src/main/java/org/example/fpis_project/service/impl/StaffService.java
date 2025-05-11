package org.example.fpis_project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.fpis_project.model.dto.StaffDto;
import org.example.fpis_project.model.entity.Business;
import org.example.fpis_project.model.entity.Role;
import org.example.fpis_project.model.entity.Staff;
import org.example.fpis_project.model.entity.User;
import org.example.fpis_project.repository.BusinessRepository;
import org.example.fpis_project.repository.StaffRepository;
import org.example.fpis_project.repository.UserRepository;
import org.example.fpis_project.util.DtoMapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository repository;
    private final StaffRepository staffRepository;
    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;

    public List<StaffDto> getStaffByBusinessId(Long businessId) {
        return repository.findByBusinessId(businessId).stream()
                .map(DtoMapperUtil::mapToStaffDto)
                .sorted()
                .toList();
    }

    public StaffDto getStaff(Long id) {
        return DtoMapperUtil.mapToStaffDto(Objects.requireNonNull(staffRepository.findById(id).orElse(null)));
    }

    public StaffDto createStaff(StaffDto staffDto) {
        Business business = businessRepository.findById(staffDto.getBusinessId())
                .orElseThrow(() -> new EntityNotFoundException("Business not found with ID: " + staffDto.getBusinessId()));


        Staff staff = Staff.builder()
                .name(staffDto.getName())
                .surname(staffDto.getSurname())
                .position(staffDto.getPosition())
                .position(staffDto.getPosition())
                .phone(staffDto.getPhone())
                .description(staffDto.getDescription())
                .business(business)
                .build();

        User user = User.builder()
                .fullname(staffDto.getName() + " " + staffDto.getSurname())
                .name(staffDto.getName())
                .surname(staffDto.getSurname())
                .phoneNumber(staffDto.getPhone())
                .role(Role.STAFF)
                .password("password")
                .build();

        userRepository.save(user);
        staffRepository.save(staff);

        return DtoMapperUtil.mapToStaffDto(staff);
    }
}
