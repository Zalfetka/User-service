package com.example.food.service;

import com.example.food.dto.PFC;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
@Slf4j
public class MacronutrientCalculatorService {

    private static final double PROTEIN_CAL = 4.0;
    private static final double FAT_CAL = 9.0;
    private static final double CARBS_CAL = 4.0;

    public PFC calculateFromCalories(double dailyNorm) {

        double proteinCalories = dailyNorm * 0.25;
        double fatCalories = dailyNorm * 0.34;
        double carbsCalories = dailyNorm * 0.41;

        double proteinGrams = proteinCalories / PROTEIN_CAL;
        double fatGrams = fatCalories / FAT_CAL;
        double carbsGrams = carbsCalories / CARBS_CAL;

        return PFC.builder()
                .calories(dailyNorm)
                .proteins(round(proteinGrams))
                .fats(round(fatGrams))
                .carbs(round(carbsGrams))
                .date(LocalDate.now())
                .build();
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
