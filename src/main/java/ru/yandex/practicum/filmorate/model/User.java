package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private final int id;
    private final String email;
    private final String login;
    private final LocalDate birthday;
    private String name;

}
