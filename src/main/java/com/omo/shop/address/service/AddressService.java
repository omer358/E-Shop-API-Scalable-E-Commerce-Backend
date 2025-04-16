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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.omo.shop.common.constants.ExceptionMessages.ADDRESS_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AddressService implements IAddressService {

    private final UserService userService;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final UserRepository userRepository;

    @Override
    public AddressDto createAddress(CreateAddressRequest request) {
        User user = getAuthenticatedUser();
        Address address = Address.builder()
                .street(request.getStreet())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .zipCode(request.getZipCode())
                .user(user)
                .build();

        return addressMapper.toDto(addressRepository.save(address));
    }

    @Override
    public AddressDto updateAddress(Long id, UpdateAddressRequest request) {
        Address address = findAddressForCurrentUserOrThrow(id);

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setZipCode(request.getZipCode());

        return addressMapper.toDto(addressRepository.save(address));
    }

    @Override
    public void deleteAddress(Long id) {
        Address address = findAddressForCurrentUserOrThrow(id);
        addressRepository.delete(address);
    }

    @Override
    public List<AddressDto> getUserAddresses() {
        Long userId = getAuthenticatedUser().getId();
        return addressRepository.findByUserId(userId)
                .stream()
                .map(addressMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDto getAddressById(Long id) {
        Address address = findAddressForCurrentUserOrThrow(id);
        return addressMapper.toDto(address);
    }

    // 🔐 Encapsulate the authorization check
    private Address findAddressForCurrentUserOrThrow(Long id) {
        User user = getAuthenticatedUser();
        return addressRepository.findById(id)
                .filter(address -> address.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException(ADDRESS_NOT_FOUND));
    }

    public User getAuthenticatedUser() {
        return userService.getAuthenticatedUser();
    }
}
