package com.omo.shop.user.service;

import com.omo.shop.exceptions.AlreadyExistsException;
import com.omo.shop.exceptions.ResourceNotFoundException;
import com.omo.shop.user.UserMapper;
import com.omo.shop.user.dto.UserDto;
import com.omo.shop.user.model.User;
import com.omo.shop.user.repository.UserRepository;
import com.omo.shop.user.request.UserCreationRequest;
import com.omo.shop.user.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto getUserDtoById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    @Override
    public UserDto createUser(UserCreationRequest userRequest) {
        return Optional.of(userRequest)
                .filter(user ->
                        !userRepository.existsByEmail(userRequest.getEmail()))
                .map(request -> {
                    User user = User.builder()
                            .firstName(userRequest.getFirstName())
                            .lastName(userRequest.getLastName())
                            .email(userRequest.getEmail())
                            .password(passwordEncoder.encode(userRequest.getPassword()))
                            .build();
                    return userMapper.toDto(userRepository.save(user));
                }).orElseThrow(() ->
                        new AlreadyExistsException(
                                "User with" + userRequest.getEmail() + "already exists!"
                        ));
    }

    @Override
    public UserDto updateUser(Long userId, UserUpdateRequest userRequest) {
        User user = userRepository.findById(userId)
                .map(existingUser -> {
                    existingUser.setFirstName(userRequest.getFirstName());
                    existingUser.setLastName((userRequest.getLastName()));
                    return userRepository.save(existingUser);
                }).orElseThrow(
                        () -> new ResourceNotFoundException("User not found")
                );
        return userMapper.toDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }
}
