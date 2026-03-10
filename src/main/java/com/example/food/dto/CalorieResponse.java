package com.example.food.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class CalorieResponse {

    private String date;
    private Double dailyNorm;
    private Double totalConsumed;
    private Double remainingCalories;


}
