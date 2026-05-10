package com.example.food.controller;

import com.example.food.dto.*;
import com.example.food.entity.UserEntity;
import com.example.food.exception.UserAlreadyExistException;
import com.example.food.security.BearerTokenGenerator;
import com.example.food.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API для аутентификации и регистрации пользователей")
public class AuthController {

    private final BearerTokenGenerator bearerTokenGenerator;
    private final AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<?> generateBearerToken(@RequestBody UserEntity user, Long userId) {
        String token = bearerTokenGenerator.generateToken(user, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("tokenType", "Bearer");
        response.put("username", user.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity <?> validateBearerToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of("valid", false, "message", "No token provided"));
        }

        String token = authHeader.substring(7);
        boolean valid = bearerTokenGenerator.validateToken(token);
        String username = bearerTokenGenerator.getUsernameFromToken(token);

        Map<String, Object> response = new HashMap<>();
        response.put("valid", valid);
        response.put("username", username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request, HttpSession session) {
        return authService.authenticate(request, session);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@RequestBody @Valid RegisterRequest request) throws UserAlreadyExistException {
        return authService.register(request);
    }
}
