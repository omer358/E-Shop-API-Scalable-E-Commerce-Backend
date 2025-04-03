package com.omo.shop.user.service;

import com.omo.shop.user.dto.UserDto;
import com.omo.shop.user.request.UserCreationRequest;
import com.omo.shop.user.request.UserUpdateRequest;

public interface IUserService {
    UserDto getUserById(Long userId);

    UserDto createUser(UserCreationRequest userRequest);

    UserDto updateUser(Long userId, UserUpdateRequest userRequest);

    void deleteUser(Long userId);

}
