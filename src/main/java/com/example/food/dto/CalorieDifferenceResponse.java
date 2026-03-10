package com.example.food.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalorieDifferenceResponse {

    private LocalDate date;
    private Double dailyNorm;
    private Double consumedCalories;
    private Double remainingCalories;
    private Double proteinNorm;
    private Double consumedProteins;
    private Double remainingProteins;
    private Double fatNorm;
    private Double consumedFats;
    private Double remainingFats;
    private Double carbsNorm;
    private Double consumedCarbs;
    private Double remainingCarbs;
}
