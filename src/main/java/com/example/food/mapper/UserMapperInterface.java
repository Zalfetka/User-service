package com.example.food.mapper;

import com.example.food.dto.RegisterRequest;
import com.example.food.entity.UserEntity;
import com.example.food.repository.UserRepo;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapperInterface {

    @AfterMapping
    default void validateUsername(
            RegisterRequest request,
            @MappingTarget UserEntity user,
            @Context UserRepo userRepo
    ) {
        String newUsername = request.getUsername();

        if (newUsername != null && !newUsername.equals(user.getUsername())) {
            if (userRepo.existsByUsername(newUsername)) {
                throw new IllegalArgumentException("Username already exists: " + newUsername);
            }
            user.setUsername(newUsername);
        }
    }
}
