package com.example.food.service;

import com.example.food.dto.CalorieDifferenceResponse;
import com.example.food.dto.CalorieIntakeRequest;
import com.example.food.dto.PFC;
import com.example.food.entity.DailyCalories;
import com.example.food.entity.UserEntity;
import com.example.food.exception.UserNotFoundException;
import com.example.food.repository.CalorieRepo;
import com.example.food.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;


@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CalorieTrackingService {

    private final UserRepo userRepo;
    private final CalorieRepo calorieRepo;
    private final CalorieCalculatorService calorieCalculatorService;
    private final MacronutrientCalculatorService macronutrientCalculatorService;

    @Transactional
    public CalorieDifferenceResponse addCaloriesAndGetDifference(Long userId, CalorieIntakeRequest request) {

        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        DailyCalories dailyCalories = getOrCreateDailyRecord(user);

        validateCalorieData(request);

        double proteins = Optional.ofNullable(request.getProteins()).orElse(0.0);
        double fats = Optional.ofNullable(request.getFats()).orElse(0.0);
        double carbs = Optional.ofNullable(request.getCarbs()).orElse(0.0);
        double calories = proteins * 4 + fats * 9 + carbs * 4;

        dailyCalories.setConsumedCalories(dailyCalories.getConsumedCalories() + calories);
        dailyCalories.setRemainingCalorie(dailyCalories.getDailyNorm() - dailyCalories.getConsumedCalories());
        dailyCalories.setConsumedProteins(dailyCalories.getConsumedProteins() + proteins);
        dailyCalories.setRemainingProteins(dailyCalories.getProteinNorm() - dailyCalories.getConsumedProteins());
        dailyCalories.setConsumedFats(dailyCalories.getConsumedFats() + fats);
        dailyCalories.setRemainingFats(dailyCalories.getFatNorm() - dailyCalories.getConsumedFats());
        dailyCalories.setConsumedCarbs(dailyCalories.getConsumedCarbs() + carbs);
        dailyCalories.setRemainingCarbs(dailyCalories.getCarbsNorm() - dailyCalories.getConsumedCarbs());

        calorieRepo.save(dailyCalories);
        return buildFullResponse(dailyCalories);
    }

    private DailyCalories getOrCreateDailyRecord(UserEntity user) {

        LocalDate today = LocalDate.now();

        return calorieRepo.findByUserAndDate(user, today)
                .orElseGet(() -> createDailyRecord(user, today));
    }

    private DailyCalories createDailyRecord(UserEntity user, LocalDate date) {

        double dailyNorm = calorieCalculatorService.calorieCalculator(user);

        PFC pfc = macronutrientCalculatorService.calculateFromCalories(dailyNorm);

        DailyCalories record = DailyCalories.builder()
                .user(user)
                .date(date)
                .dailyNorm(dailyNorm)
                .consumedCalories(0.0)
                .remainingCalorie(dailyNorm)
                .proteinNorm(pfc.getProteins())
                .fatNorm(pfc.getFats())
                .carbsNorm(pfc.getCarbs())
                .consumedProteins(0.0)
                .consumedFats(0.0)
                .consumedCarbs(0.0)
                .remainingProteins(pfc.getProteins())
                .remainingFats(pfc.getFats())
                .remainingCarbs(pfc.getCarbs())
                .build();

        calorieRepo.save(record);
        return record;
    }

    private void validateCalorieData(CalorieIntakeRequest request) {
        if (request.getProteins() != null) {
            if (request.getProteins() < 1 || request.getProteins() > 250) {
                throw new IllegalArgumentException("The amount of protein should be more than 1 and less than 250");
            }
        }
        if (request.getCarbs() != null) {
            if (request.getCarbs() < 1 || request.getCarbs() > 400) {
                throw new IllegalArgumentException("The amount of carbs should be more than 1 and less than 400");
            }
        }
        if (request.getFats() != null) {
            if (request.getFats() < 1 || request.getFats() > 250) {
                throw new IllegalArgumentException("The amount of fats should be more than 1 and less than 250");
            }
        }
    }

    private CalorieDifferenceResponse buildFullResponse(DailyCalories dailyCalories) {
        return CalorieDifferenceResponse.builder()
                .date(dailyCalories.getDate())
                .dailyNorm(round(dailyCalories.getDailyNorm()))
                .proteinNorm(round(dailyCalories.getProteinNorm()))
                .fatNorm(round(dailyCalories.getFatNorm()))
                .carbsNorm(round(dailyCalories.getCarbsNorm()))
                .consumedCalories(round(dailyCalories.getConsumedCalories()))
                .consumedProteins(round(dailyCalories.getConsumedProteins()))
                .consumedFats(round(dailyCalories.getConsumedFats()))
                .consumedCarbs(round(dailyCalories.getConsumedCarbs()))
                .remainingCalories(round(dailyCalories.getRemainingCalorie()))
                .remainingProteins(round(dailyCalories.getRemainingProteins()))
                .remainingFats(round(dailyCalories.getRemainingFats()))
                .remainingCarbs(round(dailyCalories.getRemainingCarbs()))
                .build();
    }

    private double round(double value) {
        return Math.round(value);
    }
}
