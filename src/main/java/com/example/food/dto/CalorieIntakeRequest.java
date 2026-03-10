package com.example.food.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalorieIntakeRequest {

    private String foodName;
    private Double proteins;
    private Double fats;
    private Double carbs;
}
