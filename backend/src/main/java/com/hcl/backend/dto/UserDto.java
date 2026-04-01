package com.hcl.backend.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String role;
    private boolean isBlocked;
    private boolean verified;
}
