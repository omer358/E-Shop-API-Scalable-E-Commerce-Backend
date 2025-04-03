package com.omo.shop.user;

import com.omo.shop.user.dto.UserDto;
import com.omo.shop.user.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserMapper {
    private final ModelMapper modelMapper;

    public UserDto toDto(User user){
        return modelMapper.map(user,UserDto.class);
    }
}
