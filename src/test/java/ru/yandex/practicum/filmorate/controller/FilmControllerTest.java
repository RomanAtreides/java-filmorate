package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController controller;

    @BeforeEach
    void setUp() {
        controller = new FilmController();
        createTestFilms();
    }

    @Test
    void findAll() {
    }

    @Test
    void create() {
    }

    @Test
    void put() {
    }

    void createTestFilms() {
        Film film1 = new Film(
                1,
                "film1 name",
                "film1 description",
                LocalDate.of(1985, 1, 1),
                Duration.ofMinutes(100)
        );

        controller.getFilms().put(film1.getName(), film1);
    }
}