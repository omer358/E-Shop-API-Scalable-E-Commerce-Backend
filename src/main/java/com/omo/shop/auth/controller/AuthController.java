package com.omo.shop.auth.controller;

import com.omo.shop.auth.request.AuthRequest;
import com.omo.shop.auth.service.AuthService;
import com.omo.shop.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticate(
            @RequestBody AuthRequest request
    ) {
        return ResponseEntity
                .ok(new ApiResponse("Success", service.authenticate(request)));
    }
}
