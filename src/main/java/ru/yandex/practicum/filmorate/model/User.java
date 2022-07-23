package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private long id;
    private final String email;
    private final String login;
    private final LocalDate birthday;
    @NonNull
    private String name;
    private Set<Long> friends = new HashSet<>();
}
