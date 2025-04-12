package com.omo.shop.user.service;

import com.omo.shop.common.exceptions.AlreadyExistsException;
import com.omo.shop.common.exceptions.ResourceNotFoundException;
import com.omo.shop.user.UserMapper;
import com.omo.shop.user.dto.UserDto;
import com.omo.shop.user.model.User;
import com.omo.shop.user.repository.UserRepository;
import com.omo.shop.user.request.UserCreationRequest;
import com.omo.shop.user.request.UserUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.omo.shop.common.constants.ExceptionMessages.EMAIL_TAKE;
import static com.omo.shop.common.constants.ExceptionMessages.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;
    private UserCreationRequest userCreationRequest;
    private UserUpdateRequest userUpdateRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1L)
                .firstName("Omar")
                .lastName("Dev")
                .email("omar@example.com")
                .password("encoded-pass")
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .firstName("Omar")
                .lastName("Dev")
                .email("omar@example.com")
                .build();

        userCreationRequest = new UserCreationRequest(
                "Omar", "Dev", "omar@example.com", "plain-pass"
        );

        userUpdateRequest = new UserUpdateRequest("NewOmar", "NewDev");
    }

    @Test
    @DisplayName("should return UserDto when user exists by ID")
    void getUserDtoById_shouldReturnUserDto_whenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserDtoById(1L);

        assertEquals(userDto.getEmail(), result.getEmail());
        verify(userRepository).findById(1L);
        verify(userMapper).toDto(user);
    }


    @Test
    @DisplayName("should return UserEntity when user exists by ID")
    void getUserById_shouldReturnUserEntity_whenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("should throw exception when user dto not found by ID")
    void getUserDtoById_shouldThrowResourceNotFoundException_whenUserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserDtoById(2L));

        assertEquals(USER_NOT_FOUND, ex.getMessage());
    }

    @Test
    @DisplayName("should throw exception when user not found by ID")
    void getUserById_shouldThrowResourceNotFoundException_whenUserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(2L));

        assertEquals(USER_NOT_FOUND, ex.getMessage());
    }


    @Test
    @DisplayName("should create new user when email not taken")
    void createUser_shouldCreateUser_whenEmailNotExists() {
        when(userRepository.existsByEmail(userCreationRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("plain-pass")).thenReturn("encoded-pass");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        UserDto result = userService.createUser(userCreationRequest);

        assertEquals(userDto.getEmail(), result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("should throw exception when creating user with taken email")
    void createUser_shouldThrow_whenEmailExists() {
        when(userRepository.existsByEmail(userCreationRequest.getEmail())).thenReturn(true);

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () ->
                userService.createUser(userCreationRequest));

        assertEquals(EMAIL_TAKE, exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("should update user when user exists")
    void updateUser_shouldUpdateUser_whenExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        UserDto result = userService.updateUser(1L, userUpdateRequest);

        assertEquals(userDto.getEmail(), result.getEmail());
        verify(userRepository).save(user);
        verify(userMapper).toDto(user);
    }

    @Test
    @DisplayName("should throw when updating user not found")
    void updateUser_shouldThrow_whenUserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                userService.updateUser(2L, userUpdateRequest));
    }

    @Test
    @DisplayName("should delete user by ID")
    void deleteUser_shouldCallRepository() {
        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("should return authenticated user from SecurityContext")
    void getAuthenticatedUser_shouldReturnUser_whenEmailMatches() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User result = userService.getAuthenticatedUser();

        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    @DisplayName("should throw if authenticated user email not found")
    void getAuthenticatedUser_shouldThrow_whenEmailNotFound() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("notfound@example.com");
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                userService.getAuthenticatedUser());
    }
}
