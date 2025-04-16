package com.omo.shop.address.service;

import com.omo.shop.address.dto.AddressDto;
import com.omo.shop.address.mapper.AddressMapper;
import com.omo.shop.address.model.Address;
import com.omo.shop.address.repository.AddressRepository;
import com.omo.shop.address.request.CreateAddressRequest;
import com.omo.shop.address.request.UpdateAddressRequest;
import com.omo.shop.common.exceptions.ResourceNotFoundException;
import com.omo.shop.user.model.User;
import com.omo.shop.user.repository.UserRepository;
import com.omo.shop.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.omo.shop.common.constants.ExceptionMessages.ADDRESS_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AddressService addressService;

    private User user;
    private Address address;
    private AddressDto addressDto;
    private CreateAddressRequest createRequest;
    private UpdateAddressRequest updateRequest;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).build();

        address = Address.builder()
                .id(1L)
                .street("Main St")
                .city("Metropolis")
                .state("Gotham")
                .country("USA")
                .zipCode("12345")
                .user(user)
                .build();

        addressDto = AddressDto.builder()
                .id(1L)
                .street("Main St")
                .city("Metropolis")
                .state("Gotham")
                .country("USA")
                .zipCode("12345")
                .build();

        createRequest = CreateAddressRequest.builder()
                .street("Main St")
                .city("Metropolis")
                .state("Gotham")
                .country("USA")
                .zipCode("12345")
                .build();

        updateRequest = UpdateAddressRequest.builder()
                .street("Updated St")
                .city("New City")
                .state("New State")
                .country("New Country")
                .zipCode("99999")
                .build();
    }

    @Test
    @DisplayName("Should create address when user exists")
    void createAddress_shouldSucceed_whenUserExists() {
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(addressService.getAuthenticatedUser()).thenReturn(user);
        when(addressMapper.toDto(any(Address.class))).thenReturn(addressDto);

        AddressDto result = addressService.createAddress(createRequest);

        assertNotNull(result);
        assertEquals(addressDto.getStreet(), result.getStreet());
        verify(addressRepository).save(any(Address.class));
        verify(addressMapper).toDto(any(Address.class));
    }


    @Test
    @DisplayName("Should update address when address exists")
    void updateAddress_shouldSucceed_whenAddressExists() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(addressMapper.toDto(address)).thenReturn(addressDto);
        when(addressService.getAuthenticatedUser()).thenReturn(user);


        AddressDto result = addressService.updateAddress(1L, updateRequest);

        assertNotNull(result);
        assertEquals(addressDto.getStreet(), result.getStreet());
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    @DisplayName("Should throw exception when address not found during update")
    void updateAddress_shouldThrowException_whenAddressNotFound() {

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> addressService.updateAddress(1L, updateRequest));

        assertEquals(ADDRESS_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DisplayName("Should delete address by ID if exist")
    void deleteAddress_shouldSucceed_IfAddressExist() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressService.getAuthenticatedUser()).thenReturn(user);

        addressService.deleteAddress(1L);
        verify(addressRepository).delete(any(Address.class));
    }

    @Test
    @DisplayName("Should throw exception  if address doesn't exist")
    void deleteAddress_shouldThrowException_IfAddressNotExist() {
        when(addressRepository.findById(any())).thenReturn(Optional.empty());
        when(addressService.getAuthenticatedUser()).thenReturn(user);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> addressService.deleteAddress(1L));
        assertEquals(ADDRESS_NOT_FOUND, exception.getMessage());
        verify(addressRepository, never()).deleteById(1L);
    }

    @Test
    @DisplayName("Should return list of addresses for user")
    void getUserAddresses_shouldReturnList() {
        when(addressService.getAuthenticatedUser()).thenReturn(user);

        List<Address> addressList = List.of(address);

        when(addressRepository.findByUserId(1L)).thenReturn(addressList);
        when(addressMapper.toDto(address)).thenReturn(addressDto);

        List<AddressDto> result = addressService.getUserAddresses();

        assertEquals(1, result.size());
        verify(addressRepository).findByUserId(1L);
    }

    @Test
    @DisplayName("Should return address by ID")
    void getAddressById_shouldReturnAddress() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressService.getAuthenticatedUser()).thenReturn(user);

        when(addressMapper.toDto(address)).thenReturn(addressDto);

        AddressDto result = addressService.getAddressById(1L);

        assertNotNull(result);
        assertEquals("Main St", result.getStreet());
    }

    @Test
    @DisplayName("Should throw exception when address not found by ID")
    void getAddressById_shouldThrowException_whenNotFound() {
        when(addressService.getAuthenticatedUser()).thenReturn(user);

        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> addressService.getAddressById(1L));

        assertEquals(ADDRESS_NOT_FOUND, exception.getMessage());
    }
}