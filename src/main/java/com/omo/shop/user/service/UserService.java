package com.omo.shop.user.service;

import com.omo.shop.exceptions.AlreadyExistsException;
import com.omo.shop.exceptions.ResourceNotFoundException;
import com.omo.shop.user.model.User;
import com.omo.shop.user.repository.UserRepository;
import com.omo.shop.user.request.UserCreationRequest;
import com.omo.shop.user.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    @Override
    public User createUser(UserCreationRequest userRequest) {
        return Optional.of(userRequest)
                .filter(user ->
                        !userRepository.existsByEmail(userRequest.getEmail()))
                .map(request -> {
                    User user = User.builder()
                            .firstName(userRequest.getFirstName())
                            .lastName(userRequest.getLastName())
                            .email(userRequest.getEmail())
                            .password(userRequest.getPassword())
                            .build();
                    return userRepository.save(user);
                }).orElseThrow(() ->
                        new AlreadyExistsException(
                                "User with" + userRequest.getEmail() + "already exists!"
                        ));
    }

    @Override
    public User updateUser(Long userId, UserUpdateRequest userRequest) {
        return userRepository.findById(userId)
                .map(existingUser -> {
                    existingUser.setFirstName(userRequest.getFirstName());
                    existingUser.setLastName((userRequest.getLastName()));
                    return userRepository.save(existingUser);
                }).orElseThrow(
                        () -> new ResourceNotFoundException("User not found")
                );
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
