package com.example.food.repository;

import com.example.food.entity.KafkaMessageLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KafkaMessageLogRepository extends JpaRepository<KafkaMessageLogEntity, Long> {
    List<KafkaMessageLogEntity> findByStatus(String status);
}
