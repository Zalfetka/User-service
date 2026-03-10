package com.example.food.dto;

import com.example.food.gender.ActivityLevel;
import com.example.food.gender.Gender;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserUpdateRequest {

    private String username;
    private Integer age;
    private Gender gender;
    private Float weight;
    private Float height;
    private ActivityLevel activity;
    private Double calorieNorm;
    private Double proteinNorm;
    private Double fatNorm;
    private Double carbsNorm;
}
