package com.example.food.repository;

import com.example.food.entity.DailyCalories;
import com.example.food.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CalorieRepo extends JpaRepository <DailyCalories, Long> {
    Optional<DailyCalories> findByUserAndDate (UserEntity calorie, LocalDate date);

    List<DailyCalories> findAllByUserAndDate(UserEntity user, LocalDate date);

    List<DailyCalories> findAllByUser(UserEntity user);
}
