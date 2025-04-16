package com.omo.shop.address.service;

import com.omo.shop.address.dto.AddressDto;
import com.omo.shop.address.mapper.AddressMapper;
import com.omo.shop.address.model.Address;
import com.omo.shop.address.repository.AddressRepository;
import com.omo.shop.address.request.CreateAddressRequest;
import com.omo.shop.address.request.UpdateAddressRequest;
import com.omo.shop.common.constants.ExceptionMessages;
import com.omo.shop.common.exceptions.ResourceNotFoundException;
import com.omo.shop.user.model.User;
import com.omo.shop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.omo.shop.common.constants.ExceptionMessages.ADDRESS_NOT_FOUND;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class AddressService implements IAddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final UserRepository userRepository;

    @Override
    public AddressDto createAddress(CreateAddressRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.USER_NOT_FOUND));

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
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ADDRESS_NOT_FOUND));

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setZipCode(request.getZipCode());

        return addressMapper.toDto(addressRepository.save(address));
    }

    @Override
    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }

    @Override
    public List<AddressDto> getUserAddresses(Long userId) {
        return addressRepository.findByUserId(userId)
                .stream()
                .map(addressMapper::toDto)
                .collect(toList());
    }

    @Override
    public AddressDto getAddressById(Long id) {
        return addressMapper.toDto(
                addressRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(ADDRESS_NOT_FOUND))
        );
    }
}
