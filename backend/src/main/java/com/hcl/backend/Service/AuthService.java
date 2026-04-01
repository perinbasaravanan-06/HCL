package com.hcl.backend.service;

import com.hcl.backend.dto.AuthResponse;
import com.hcl.backend.dto.LoginRequest;
import com.hcl.backend.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
