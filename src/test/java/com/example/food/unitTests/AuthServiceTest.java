package com.example.food.unitTests;

import com.example.food.dto.AuthResponse;
import com.example.food.dto.PFC;
import com.example.food.dto.RegisterRequest;
import com.example.food.entity.UserEntity;
import com.example.food.mapper.UserMapper;
import com.example.food.repository.KafkaMessageLogRepository;
import com.example.food.repository.MongoRepo;
import com.example.food.repository.UserRepo;
import com.example.food.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private TokenCacheService tokenCacheService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepo userRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserProducer userProducer;
    @Mock
    private KafkaMessageLogRepository kafkaMessageLogRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private MacronutrientCalculatorService macronutrientCalculatorService;
    @Mock
    private CalorieCalculatorService calorieCalculatorService;
    @Mock
    private MongoRepo mongoRepo;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("test");
        request.setPassword("123");
        request.setWeight(70.0F);
        request.setHeight(180.0F);
        request.setAge(25);

        when(userRepo.findByUsername("test"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("123")).thenReturn("encoded");

        when(calorieCalculatorService.calorieCalculator((UserEntity) any()))
                .thenReturn(2000.0);

        when(macronutrientCalculatorService.calculateFromCalories(2000.0))
                .thenReturn(new PFC(2000.0, 150.0, 70.0, 250.0, LocalDate.now()));

        UserEntity savedUser = UserEntity.builder()
                .id(1L)
                .username("test")
                .build();

        when(userRepo.save(any(UserEntity.class))).thenReturn(savedUser);
        when(tokenCacheService.getOrCreateToken("test", 1L))
                .thenReturn("token123");

        when(objectMapper.writeValueAsString(any()))
                .thenReturn("{}");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("test", response.getUsername());
        assertEquals("token123", response.getToken());

        verify(userRepo).save(any());
        verify(calorieCalculatorService).calorieCalculator((UserEntity) any());
        verify(macronutrientCalculatorService).calculateFromCalories(2000.0);
        verify(tokenCacheService).getOrCreateToken("test", 1L);
        verify(kafkaMessageLogRepository).save(any());
    }
}
