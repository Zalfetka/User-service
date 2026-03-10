package com.example.food.controller;

import com.example.food.dto.*;
import com.example.food.security.CheckOwnership;
import com.example.food.service.CalorieTrackingService;
import com.example.food.service.MacronutrientCalculatorService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calories")
@RequiredArgsConstructor
public class CalorieController {

    private final CalorieTrackingService calorieTrackingService;
    private final MacronutrientCalculatorService macronutrientService;


    @CheckOwnership
    @PostMapping("/intake/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public CalorieDifferenceResponse calorieDifferenceResponse (@PathVariable Long userId, @Valid @RequestBody CalorieIntakeRequest request){
        return calorieTrackingService.addCaloriesAndGetDifference(userId, request);
    }

    @CheckOwnership
    @GetMapping("/calculate")
    @ResponseStatus(HttpStatus.OK)
    public PFC calculateMacronutrients (HttpSession session){
        Long userId = (Long) session.getAttribute("userId");
        return macronutrientService.calculateMacronutrients(userId);
    }

}
