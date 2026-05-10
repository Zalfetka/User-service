package com.example.food.service;

import com.example.food.dto.*;
import com.example.food.entity.KafkaMessageLogEntity;
import com.example.food.entity.UserEntity;
import com.example.food.exception.UserAlreadyExistException;
import com.example.food.exception.UserNotFoundException;
import com.example.food.gender.Role;
import com.example.food.gender.Status;
import com.example.food.mapper.UserMapper;
import com.example.food.repository.KafkaMessageLogRepository;
import com.example.food.repository.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
@Builder
public class AuthService {

    private final TokenCacheService tokenCacheService;
    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserProducer userProducer;
    private final KafkaMessageLogRepository kafkaMessageLogRepository;
    private final ObjectMapper objectMapper;
    private final MacronutrientCalculatorService macronutrientCalculatorService;
    private final CalorieCalculatorService calorieCalculatorService;

    private AuthResponse generaleAuthResponse(String username, Long userId) {
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        String token = tokenCacheService.getOrCreateToken(username, userId);

        return AuthResponse.builder()
                .token(token)
                .username(username)
                .userId(user.getId())
                .role(Role.USER)
                .build();

    }

    public AuthResponse authenticate(LoginRequest request, HttpSession session) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        session.setAttribute("username", user.getUsername());
        session.setAttribute("userId", user.getId());
        log.info("Login attempt for user: {}", request.getUsername());
        log.info("=== BEFORE userProducer.sendUser ===");
        log.info("userProducer bean: {}", userProducer);
        createUser(user);
        return generaleAuthResponse(user.getUsername(), user.getId());
    }

    public AuthResponse register (RegisterRequest request) throws UserAlreadyExistException {
        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("Username already exists");
        }

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .height(request.getHeight())
                .weight(request.getWeight())
                .age(request.getAge())
                .gender(request.getGender())
                .activity(request.getActivity())
                .role(request.getRole() != null ? request.getRole() : Role.USER)
                .build();

        double dailyNorm = calorieCalculatorService.calorieCalculator(user);
        PFC pfc = macronutrientCalculatorService.calculateFromCalories(dailyNorm);
        user.setCaloriesNorm(dailyNorm);
        user.setProteinNorm(pfc.getProteins());
        user.setFatNorm(pfc.getFats());
        user.setCarbsNorm(pfc.getCarbs());
        userMapper.updateUserFromRequest(request, user);
        UserEntity savedUser = createUser(user);
        String token = tokenCacheService.getOrCreateToken(savedUser.getUsername(), savedUser.getId());
        return AuthResponse.builder()
                .token(token)
                .username(savedUser.getUsername())
                .userId(savedUser.getId())
                .role(savedUser.getRole())
                .build();
    }

    public UserResponse updateUser(Long userId, RegisterRequest request) {
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        userMapper.updateUserFromRequest(request, user);
        UserEntity updatedUser = userRepo.save(user);
        log.info("User updated successfully: {}", updatedUser.getUsername());
        return mapToResponse(updatedUser);
    }

    private UserResponse mapToResponse(UserEntity user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .age(user.getAge())
                .weight(user.getWeight())
                .height(user.getHeight())
                .gender(user.getGender())
                .activity(user.getActivity())
                .build();
    }

    public UserEntity createUser(UserEntity user) {
        UserEntity saved = userRepo.save(user);

        UserDTO event = UserDTO.builder()
                .eventId(UUID.randomUUID().toString())
                .timestamp(LocalDate.now())
                .userId(saved.getId())
                .caloriesNorm(saved.getCaloriesNorm())
                .proteinNorm(saved.getProteinNorm())
                .fatNorm(saved.getFatNorm())
                .carbsNorm(saved.getCarbsNorm())
                .weight(saved.getWeight())
                .version(1)
                .status(Status.ACTIVE)
                .build();

        KafkaMessageLogEntity logEntity = new KafkaMessageLogEntity();
        logEntity.setPayload(convertToJson(event));
        logEntity.setStatus("PENDING");
        logEntity.setSentAt(LocalDateTime.now());
        logEntity.setUpdatedAt(LocalDateTime.now());
        kafkaMessageLogRepository.save(logEntity);
        return saved;
    }

    private String convertToJson(UserDTO dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (Exception e) {
            throw new RuntimeException("JSON serialization error", e);
        }
    }
}