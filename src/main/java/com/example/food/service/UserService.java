package com.example.food.service;

import com.example.food.dto.UserModel;
import com.example.food.exception.UserNotFoundException;
import com.example.food.repository.UserRepo;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;

    public UserModel getOne(Long id) throws UserNotFoundException {
        return userRepo.findById(id)
                .map(UserModel::toModel)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id=" + id + " не найден"));
    }

    @NonNull
    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public Long delete(Long id) {
        userRepo.deleteById (id);
        return id;
    }
}
