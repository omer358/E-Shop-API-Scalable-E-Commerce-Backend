package com.omo.shop.address.controller;

import com.omo.shop.address.dto.AddressDto;
import com.omo.shop.address.request.CreateAddressRequest;
import com.omo.shop.address.request.UpdateAddressRequest;
import com.omo.shop.address.service.IAddressService;
import com.omo.shop.common.exceptions.ResourceNotFoundException;
import com.omo.shop.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final IAddressService addressService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createAddress(@RequestBody CreateAddressRequest request) {
        try {
            AddressDto address = addressService.createAddress(request);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Address created", address));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), NOT_FOUND));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateAddress(
            @PathVariable(name = "id") Long addressId,
            @RequestBody UpdateAddressRequest request
    ) {
        try {
            AddressDto updated = addressService.updateAddress(addressId, request);
            return ResponseEntity.ok(new ApiResponse("Address updated", updated));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), NOT_FOUND));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getUserAddresses() {
        List<AddressDto> addresses = addressService.getUserAddresses();
        return ResponseEntity.ok(new ApiResponse("User addresses", addresses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getAddressById(@PathVariable(name = "id") Long addressId) {
        try {
            AddressDto address = addressService.getAddressById(addressId);
            return ResponseEntity.ok(new ApiResponse("Address details", address));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), NOT_FOUND));        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAddress(@PathVariable Long id) {
        try {
            addressService.deleteAddress(id);
            return ResponseEntity.ok(new ApiResponse("Address deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), NOT_FOUND));        }
    }
}
