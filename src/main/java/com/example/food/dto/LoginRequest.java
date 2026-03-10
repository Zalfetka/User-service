package com.example.food.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private Long userId;
    private String username;
    private String password;
}
