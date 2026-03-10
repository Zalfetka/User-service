package com.example.food.dto;

import com.example.food.entity.UserEntity;
import com.example.food.gender.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserModel {

    private Long id;
    private String username;
    private String password;
    private float weight;
    private float height;
    private int age;
    private Gender gender;

    public static UserModel toModel (UserEntity entity) {
        UserModel model = new UserModel();
        model.setId(entity.getId());
        model.setUsername(entity.getUsername());
        model.setPassword(entity.getPassword());
        model.setWeight(entity.getWeight());
        model.setHeight(entity.getHeight());
        model.setAge(entity.getAge());
        model.setGender(entity.getGender());
        return model;
    }
}
