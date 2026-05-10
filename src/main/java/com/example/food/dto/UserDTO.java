package com.example.food.dto;

import com.example.food.gender.ActivityLevel;
import com.example.food.gender.Gender;
import com.example.food.gender.Role;
import com.example.food.gender.Status;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String eventId;
    private LocalDate timestamp;
    private Long userId;
    private Double caloriesNorm;
    private Double proteinNorm;
    private Double fatNorm;
    private Double carbsNorm;
    private Float weight;
    private Integer version;
    private Status status;
}