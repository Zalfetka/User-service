package com.example.food.service;

import com.example.food.dto.UserDTO;
import com.example.food.entity.UserEntity;
import com.example.food.gender.ActivityLevel;
import com.example.food.gender.Gender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CalorieCalculatorService {

    public double calculateBMR(UserEntity user) {
        if (user.getGender() == Gender.MALE) {
            return (10 * user.getWeight()) + (6.25 * user.getHeight()) - (5 * user.getAge()) + 5;
        } else {
            return (10 * user.getWeight()) + (6.25 * user.getHeight()) - (5 * user.getAge()) - 161;
        }
    }

    private double calculateTDEE(double bmr, ActivityLevel activityLevel) {
        return switch (activityLevel) {
            case SEDENTARY -> bmr * 1.2;
            case LIGHTLY_ACTIVE -> bmr * 1.375;
            case MODERATELY_ACTIVE -> bmr * 1.55;
            case VERY_ACTIVE -> bmr * 1.725;
            case EXTRA_ACTIVE -> bmr * 1.9;
        };
    }
    public double calorieCalculator (UserEntity user) {

        ActivityLevel activityLevel = user.getActivity();
        if (activityLevel == null) {
            activityLevel = ActivityLevel.MODERATELY_ACTIVE;
            log.debug("Activity level not set for user {}, using default: {}",
                    user.getId(), activityLevel);
        }

        double bmr = calculateBMR(user);
        log.debug("BMR calculated for user {}: {} kcal", user.getId(), Math.round(bmr));

        double tdee = calculateTDEE(bmr, activityLevel);

        log.info("Daily calories for user {}: {} kcal (activity: {})",
                user.getId(), Math.round(tdee), activityLevel);

        return Math.round(tdee * 100.0) / 100.0;
    }
}
