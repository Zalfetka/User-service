package com.example.food.dto;

import com.example.food.gender.ActivityLevel;
import com.example.food.gender.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private Integer age;
    private Gender gender;
    private Float weight;
    private Float height;
    private ActivityLevel activity;
}
