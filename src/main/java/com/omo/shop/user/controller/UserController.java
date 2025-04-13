package com.omo.shop.user.controller;

import com.omo.shop.common.exceptions.AlreadyExistsException;
import com.omo.shop.common.exceptions.ResourceNotFoundException;
import com.omo.shop.common.response.ApiResponse;
import com.omo.shop.user.dto.UserDto;
import com.omo.shop.user.request.UserCreationRequest;
import com.omo.shop.user.request.UserUpdateRequest;
import com.omo.shop.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            UserDto userDto = userService.getUserDtoById(userId);
            return ResponseEntity.ok(new ApiResponse("success", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), NOT_FOUND));
        }
    }

    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse> createNewUser(@RequestBody UserCreationRequest userRequest) {
        try {
            UserDto userDto = userService.createUser(userRequest);
            return ResponseEntity.ok(new ApiResponse("user created successfuly!", userDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), CONFLICT));
        }
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequest userUpdateRequest) {
        try {
            UserDto userDto = userService.updateUser(userId, userUpdateRequest);
            return ResponseEntity.ok(new ApiResponse("updated successfully", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), NOT_FOUND));
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(
            @PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse("Deleted successfully", null));
    }
}
