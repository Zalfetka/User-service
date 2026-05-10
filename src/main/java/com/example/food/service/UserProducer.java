package com.example.food.service;

import com.example.food.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "user-topic";

    public void sendUser(UserDTO user) {
        try {
            String json = objectMapper.writeValueAsString(user);
            log.debug("Sending to Kafka: {}", json);
            kafkaTemplate.send(TOPIC, json);
        } catch (Exception e) {
            log.error("Error converting UserDTO to JSON", e);
        }
    }
}
