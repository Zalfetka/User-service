package com.example.food.gender;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityLevel {

    SEDENTARY("Сидячий образ жизни", 1.05),
    LIGHTLY_ACTIVE("Легкая активность", 1.1),
    MODERATELY_ACTIVE("Умеренная активность", 1.15),
    VERY_ACTIVE("Высокая активность", 1.2),
    EXTRA_ACTIVE("Очень высокая активность", 1.25);

    private final String description;
    private final double multiplier;
}
