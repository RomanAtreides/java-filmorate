package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private final int id;
    private final String email;
    private final String login;
    private final LocalDate birthday;
    private String name;

}
