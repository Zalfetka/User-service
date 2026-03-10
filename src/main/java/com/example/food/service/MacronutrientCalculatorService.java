package com.example.food.service;

import com.example.food.dto.PFC;
import com.example.food.entity.DailyCalories;
import com.example.food.entity.UserEntity;
import com.example.food.exception.UserAlreadyExistException;
import com.example.food.exception.UserNotFoundException;
import com.example.food.repository.CalorieRepo;
import com.example.food.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class MacronutrientCalculatorService {

    private final UserRepo userRepo;
    private final CalorieRepo caloriesRepo;
    private final CalorieCalculatorService calorieCalculatorService;

    private static final double PROTEIN_CAL = 4.0;
    private static final double FAT_CAL = 9.0;
    private static final double CARBS_CAL = 4.0;

    @Transactional
    public PFC calculateMacronutrients(Long userId) {

        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        LocalDate today = LocalDate.now();

        double dailyNorm = calorieCalculatorService.calorieCalculator(user);

        PFC pfc = calculateFromCalories(dailyNorm);

        DailyCalories record = caloriesRepo.findByUserAndDate(user, today)
                .orElseGet(() -> DailyCalories.builder()
                        .user(user)
                        .date(today)
                        .consumedCalories(0.0)
                        .build());

        record.setDailyNorm(dailyNorm);
        record.setRemainingCalorie(dailyNorm);

        record.setProteinNorm(pfc.getProteins());
        record.setFatNorm(pfc.getFats());
        record.setCarbsNorm(pfc.getCarbs());

        caloriesRepo.save(record);

        log.info("Daily nutrition calculated for user {} on {}", userId, today);

        return pfc;
    }

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
