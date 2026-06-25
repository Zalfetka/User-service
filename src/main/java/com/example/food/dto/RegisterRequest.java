package com.example.food.dto;

import com.example.food.gender.ActivityLevel;
import com.example.food.gender.Gender;
import com.example.food.gender.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterRequest {

    private Long userId;
    private String username;
    private String password;
    private Integer age;
    private Gender gender;
    private Float weight;
    private Float height;
    private ActivityLevel activity;
    private Role role;
}
