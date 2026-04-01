package com.hcl.backend.service.impl;

import com.hcl.backend.Entity.User;
import com.hcl.backend.dto.AuthResponse;
import com.hcl.backend.dto.LoginRequest;
import com.hcl.backend.dto.RegisterRequest;
import com.hcl.backend.dto.UserDto;
import com.hcl.backend.exception.BadRequestException;
import com.hcl.backend.repository.UserRepository;
import com.hcl.backend.security.JwtUtils;
import com.hcl.backend.service.AuthService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole("USER"); // Default role
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        String jwtToken = jwtUtils.generateToken(savedUser);

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(savedUser, userDto);
        userDto.setUsername(savedUser.getEmail());
        userDto.setName(savedUser.getName());
        userDto.setRole(savedUser.getRole());

        return AuthResponse.builder()
                .token(jwtToken)
                .user(userDto)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));

        String jwtToken = jwtUtils.generateToken(user);

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        userDto.setUsername(user.getEmail());
        userDto.setName(user.getName());
        userDto.setRole(user.getRole());

        return AuthResponse.builder()
                .token(jwtToken)
                .user(userDto)
                .build();
    }
}
