package com.example.food.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TokenInfo {

    private String token;
    private Long userId;
    private LocalDate createdAt;
}
