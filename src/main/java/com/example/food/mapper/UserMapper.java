package com.example.food.mapper;

import com.example.food.dto.RegisterRequest;
import com.example.food.entity.UserEntity;
import org.mapstruct.*;


@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class UserMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    public abstract void updateUserFromRequest(RegisterRequest request, @MappingTarget UserEntity user);

    @AfterMapping
    protected void validateAge(RegisterRequest request) {
        if (request.getAge() != null && (request.getAge() < 10 || request.getAge() > 120)) {
            throw new IllegalArgumentException("Age must be between 10 and 120");
        }
    }

    @AfterMapping
    protected void validateWeight(RegisterRequest request) {
        if (request.getWeight() != null && (request.getWeight() < 20 || request.getWeight() > 300)) {
            throw new IllegalArgumentException("Weight must be between 20 and 300 kg");
        }
    }

    @AfterMapping
    protected void validateHeight(RegisterRequest request) {
        if (request.getHeight() != null && (request.getHeight() < 100 || request.getHeight() > 250)) {
            throw new IllegalArgumentException("Height must be between 100 and 250 cm");
        }
    }

}
