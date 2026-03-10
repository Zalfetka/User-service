package com.example.food.controller;

import com.example.food.dto.UserResponse;
import com.example.food.dto.UserUpdateRequest;
import com.example.food.exception.UserNotFoundException;
import com.example.food.security.CheckOwnership;
import com.example.food.service.AuthService;
import com.example.food.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<?> getOneUser (@RequestParam Long id) {
        try {
            return ResponseEntity.ok(userService.getOne(id));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <?> deleteUser (@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.delete(id));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @CheckOwnership
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        return authService.updateUser(id, request);
    }
}
