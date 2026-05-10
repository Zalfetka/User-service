package com.example.food.dto;

import com.example.food.gender.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String username;
    private Long userId;
    private Role role;
}
