package org.example.fpis_project.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.fpis_project.model.dto.BusinessDto;
import org.example.fpis_project.model.entity.Business;
import org.example.fpis_project.model.entity.Role;
import org.example.fpis_project.model.entity.User;
import org.example.fpis_project.repository.BusinessRepository;
import org.example.fpis_project.repository.UserRepository;
import org.example.fpis_project.service.impl.DefaultBusinessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultBusinessServiceTest {

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DefaultBusinessService businessService;

    private User testUser;
    private Business testBusiness;
    private BusinessDto testBusinessDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setRole(Role.USER);

        testBusiness = Business.builder()
                .id(1L)
                .name("Test Business")
                .address("123 Test St")
                .phone("555-1234")
                .description("Test Description")
                .topic("Test Topic")
                .owner(testUser)
                .build();

        testBusinessDto = BusinessDto.builder()
                .id(1L)
                .name("Test Business")
                .address("123 Test St")
                .phone("555-1234")
                .description("Test Description")
                .topic("Test Topic")
                .ownerId(1L)
                .build();
    }

    @Test
    void testGetAllBusinessesWhenBusinessesExist() {
        List<Business> businesses = Arrays.asList(testBusiness);
        when(businessRepository.findAll()).thenReturn(businesses);

        List<BusinessDto> result = businessService.getAllBusinesses();

        assertEquals(1, result.size());
        assertEquals(testBusiness.getId(), result.get(0).getId());
        assertEquals(testBusiness.getName(), result.get(0).getName());
        verify(businessRepository, times(1)).findAll();
    }

    @Test
    void testGetAllBusinessesWhenNoBusinessesExist() {
        when(businessRepository.findAll()).thenReturn(Collections.emptyList());

        List<BusinessDto> result = businessService.getAllBusinesses();

        assertTrue(result.isEmpty());
        verify(businessRepository, times(1)).findAll();
    }

    @Test
    void testGetBusinessByIdWhenBusinessExists() {
        when(businessRepository.findById(1L)).thenReturn(Optional.of(testBusiness));

        Optional<BusinessDto> result = businessService.getBusinessById(1L);

        assertTrue(result.isPresent());
        assertEquals(testBusiness.getId(), result.get().getId());
        assertEquals(testBusiness.getName(), result.get().getName());
        verify(businessRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBusinessByIdWhenBusinessDoesNotExist() {
        when(businessRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<BusinessDto> result = businessService.getBusinessById(1L);

        assertFalse(result.isPresent());
        verify(businessRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateBusinessWhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(businessRepository.save(any(Business.class))).thenReturn(testBusiness);

        BusinessDto result = businessService.createBusiness(testBusinessDto);

        assertEquals(testBusinessDto.getId(), result.getId());
        assertEquals(testBusinessDto.getName(), result.getName());
        assertEquals(Role.OWNER, testUser.getRole());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(testUser);
        verify(businessRepository, times(1)).save(any(Business.class));
    }

    @Test
    void testCreateBusinessWhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> businessService.createBusiness(testBusinessDto));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
        verify(businessRepository, never()).save(any(Business.class));
    }

    @Test
    void testDeleteBusinessWhenCalled() {
        doNothing().when(businessRepository).deleteById(1L);

        businessService.deleteBusiness(1L);

        verify(businessRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateBusinessWhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(businessRepository.save(any(Business.class))).thenReturn(testBusiness);

        BusinessDto result = businessService.updateBusiness(testBusinessDto);

        assertEquals(testBusinessDto.getId(), result.getId());
        assertEquals(testBusinessDto.getName(), result.getName());
        verify(userRepository, times(1)).findById(1L);
        verify(businessRepository, times(1)).save(any(Business.class));
    }

    @Test
    void testUpdateBusinessWhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> businessService.updateBusiness(testBusinessDto));
        verify(userRepository, times(1)).findById(1L);
        verify(businessRepository, never()).save(any(Business.class));
    }

    @Test
    void testGetOwnerByBusinessIdWhenBusinessExists() {
        when(businessRepository.findById(1L)).thenReturn(Optional.of(testBusiness));

        User result = businessService.getOwnerByBusinessId(1L);

        assertEquals(testUser.getId(), result.getId());
        verify(businessRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOwnerByBusinessIdWhenBusinessDoesNotExist() {
        when(businessRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> businessService.getOwnerByBusinessId(1L));
        verify(businessRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBusinessByUserIdWhenUserAndBusinessExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(businessRepository.findAllByOwner(testUser)).thenReturn(Collections.singletonList(testBusiness));

        BusinessDto result = businessService.getBusinessByUserId(1L);

        assertEquals(testBusiness.getId(), result.getId());
        assertEquals(testBusiness.getName(), result.getName());
        verify(userRepository, times(1)).findById(1L);
        verify(businessRepository, times(1)).findAllByOwner(testUser);
    }

    @Test
    void testGetBusinessByUserIdWhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> businessService.getBusinessByUserId(1L));
        verify(userRepository, times(1)).findById(1L);
        verify(businessRepository, never()).findAllByOwner(any(User.class));
    }

    @Test
    void testGetBusinessByUserIdWhenUserExistsButNoBusinessFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(businessRepository.findAllByOwner(testUser)).thenReturn(Collections.emptyList());

        assertThrows(IndexOutOfBoundsException.class, () -> businessService.getBusinessByUserId(1L));
        verify(userRepository, times(1)).findById(1L);
        verify(businessRepository, times(1)).findAllByOwner(testUser);
    }
}
