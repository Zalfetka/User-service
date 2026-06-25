package com.example.food.repository;

import com.example.food.dto.DailyCaloriesMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoRepo extends MongoRepository<DailyCaloriesMongo, String> {
}
