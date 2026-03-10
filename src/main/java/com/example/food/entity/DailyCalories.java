package com.example.food.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "date"})
})
@Builder
public class DailyCalories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn (name = "user_id")
    private UserEntity user;
    @Column
    private Double dailyNorm;
    @Column(nullable = false)
    private Double consumedCalories;
    @Column
    private Double remainingCalorie;
    @Column
    private Double proteinNorm;
    @Column
    private Double consumedProteins;
    @Column
    private Double remainingProteins;
    @Column
    private Double carbsNorm;
    @Column
    private Double consumedCarbs;
    @Column
    private Double remainingCarbs;
    @Column
    private Double fatNorm;
    @Column
    private Double consumedFats;
    @Column
    private Double remainingFats;
    @Column
    private Double carbsCalories;
    @Column
    private Double proteinCalories;
    @Column
    private Double fatCalories;
    @Column(nullable = false)
    private LocalDate createdAt;
    @Column
    private LocalDate date;
    @Column
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }

}
