package com.omo.shop.address.mapper;

import com.omo.shop.address.dto.AddressDto;
import com.omo.shop.address.model.Address;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressMapper {
    private final ModelMapper modelMapper;

    public AddressDto toDto(Address address) {
        return modelMapper.map(address, AddressDto.class);
    }

    public Address toEntity(AddressDto dto) {
        return modelMapper.map(dto, Address.class);
    }
}
