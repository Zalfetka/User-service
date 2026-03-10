package com.example.food.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MacronutrientRequest {

    private Double dailyCalories;
    private Double weightKg;
    private Double heightCm;
    private Integer age;
    private String gender;
}
