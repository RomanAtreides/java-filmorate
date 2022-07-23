package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private long id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final long duration;
    private Set<User> likes = new HashSet<>();
}
