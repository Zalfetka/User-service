package com.example.food.dto;

import com.example.food.gender.Role;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyCaloriesMongo {
    @Id
    private String id;
    private String token;
    private String username;
    private Long userId;
    private Role role;
    private LocalDate date;
}
