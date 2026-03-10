package com.example.food.repository;

import com.example.food.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UserRepo extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
}
