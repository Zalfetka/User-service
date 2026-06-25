package com.example.food.integtationTests;

import com.example.food.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"user-topic"})
class KafkaIntegrationTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private UserRepo userRepo;


    @Test
    void shouldConsumeMessageAndSaveToDb() {
        String message = """
        {
          "eventId": "evt-12345-001",
          "timestamp": "2026-05-26",
          "userId": 80,
          "caloriesNorm": 2500.0,
          "proteinNorm": 150.0,
          "fatNorm": 70.0,
          "carbsNorm": 300.0,
          "weight": 75.5,
          "version": 1,
          "status": "ACTIVE"
        }
        """;

        kafkaTemplate.send("user-topic", message);
        await()
                .atMost(5, TimeUnit.SECONDS)
                .pollDelay(1, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    assertFalse(userRepo.findAll().isEmpty());
                });
    }
}
