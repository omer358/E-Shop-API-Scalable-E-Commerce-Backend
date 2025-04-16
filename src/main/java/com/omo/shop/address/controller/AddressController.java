package com.omo.shop.address.controller;

import com.omo.shop.address.dto.AddressDto;
import com.omo.shop.address.request.CreateAddressRequest;
import com.omo.shop.address.request.UpdateAddressRequest;
import com.omo.shop.address.service.IAddressService;
import com.omo.shop.common.response.ApiResponse;
import com.omo.shop.user.model.User;
import com.omo.shop.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("${api.prefix}/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final IAddressService addressService;
    private final IUserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse> createAddress(@RequestBody CreateAddressRequest request) {
        User user = userService.getAuthenticatedUser();
        AddressDto address = addressService.createAddress(request, user.getId());
        return ResponseEntity.status(CREATED).body(new ApiResponse("Address created", address));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateAddress(
            @PathVariable Long id,
            @RequestBody UpdateAddressRequest request
    ) {
        AddressDto updated = addressService.updateAddress(id, request);
        return ResponseEntity.ok(new ApiResponse("Address updated", updated));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserAddresses(@PathVariable Long userId) {
        List<AddressDto> addresses = addressService.getUserAddresses(userId);
        return ResponseEntity.ok(new ApiResponse("User addresses", addresses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getAddressById(@PathVariable Long id) {
        AddressDto address = addressService.getAddressById(id);
        return ResponseEntity.ok(new ApiResponse("Address details", address));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok(new ApiResponse("Address deleted", null));
    }
}
