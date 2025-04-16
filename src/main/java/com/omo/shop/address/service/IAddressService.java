package com.omo.shop.address.service;

import com.omo.shop.address.dto.AddressDto;
import com.omo.shop.address.request.CreateAddressRequest;
import com.omo.shop.address.request.UpdateAddressRequest;

import java.util.List;

public interface IAddressService {
    AddressDto createAddress(CreateAddressRequest request);
    AddressDto updateAddress(Long id, UpdateAddressRequest request);
    void deleteAddress(Long id);
    List<AddressDto> getUserAddresses();
    AddressDto getAddressById(Long id);
}
