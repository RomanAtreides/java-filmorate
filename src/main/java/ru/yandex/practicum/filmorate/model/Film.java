package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    private final int id;
    private final String description;
    private final LocalDate releaseDate;
    private final Duration duration;
    private String name;
}
