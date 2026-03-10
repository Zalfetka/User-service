package com.example.food.gender;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {

    MALE("мужчина", "male", "м"),
    FEMALE("женщина", "female", "ж");

    private final String russianName;
    private final String englishName;
    private final String shortName;

}
