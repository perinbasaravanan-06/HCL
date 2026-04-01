package com.hcl.backend.controller;

import com.hcl.backend.dto.ApiResponse;
import com.hcl.backend.dto.AuthResponse;
import com.hcl.backend.dto.LoginRequest;
import com.hcl.backend.dto.RegisterRequest;
import com.hcl.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(new ApiResponse(true, "User logged in successfully", response));
    }
}
