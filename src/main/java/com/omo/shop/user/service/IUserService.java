package com.omo.shop.user.service;

import com.omo.shop.user.model.User;
import com.omo.shop.user.request.UserCreationRequest;
import com.omo.shop.user.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);

    User createUser(UserCreationRequest userRequest);

    User updateUser(Long userId, UserUpdateRequest userRequest);

    void deleteUser(Long userId);

}
