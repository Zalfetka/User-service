package com.example.food.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class PFC {

    private Double calories;
    private Double proteins;
    private Double carbs;
    private Double fats;
    private LocalDate date;

}
