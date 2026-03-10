package com.example.food.service;

import com.example.food.dto.*;
import com.example.food.entity.UserEntity;
import com.example.food.exception.UserAlreadyExistException;
import com.example.food.exception.UserNotFoundException;
import com.example.food.mapper.UserMapper;
import com.example.food.repository.UserRepo;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {

    private final TokenCacheService tokenCacheService;
    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    private AuthResponse generaleAuthResponse(String username, Long userId) {
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        String token = tokenCacheService.getOrCreateToken(username, userId);

        return AuthResponse.builder()
                .token(token)
                .username(username)
                .userId(user.getId())
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

        return generaleAuthResponse(user.getUsername(), user.getId());
    }

    public AuthResponse register (RegisterRequest request) throws UserAlreadyExistException {
        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("Username already exists");
        }
        validateUserData(request);
        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .height(request.getHeight())
                .weight(request.getWeight())
                .age(request.getAge())
                .gender(request.getGender())
                .activity(request.getActivity())
                .build();

        UserEntity savedUser = userRepo.save(user);
        String token = tokenCacheService.getOrCreateToken(request.getUsername(), request.getUserId());

        return AuthResponse.builder()
                .token(token)
                .username(savedUser.getUsername())
                .userId(savedUser.getId())
                .build();

    }
    private void validateUserData(RegisterRequest request) {
        if (request.getAge() != null) {
            if (request.getAge() < 10 || request.getAge() > 120) {
                throw new IllegalArgumentException("Age must be between 10 and 120 years");
            }
        }

        if (request.getWeight() != null) {
            if (request.getWeight() < 20 || request.getWeight() > 300) {
                throw new IllegalArgumentException("Weight must be between 20 and 300 kg");
            }
        }

        if (request.getHeight() != null) {
            if (request.getHeight() < 100 || request.getHeight() > 250) {
                throw new IllegalArgumentException("Height must be between 100 and 250 cm");
            }
        }
    }
    public UserResponse updateUser(Long userId, UserUpdateRequest request) {
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
}
