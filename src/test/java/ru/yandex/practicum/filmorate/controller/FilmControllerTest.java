package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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
    void shouldThrowExceptionIfNameAlreadyExists() {
        Film film2 = new Film(
                2,
                "film1 name",
                "film2 description",
                LocalDate.of(1986, 7, 1),
                Duration.ofMinutes(110)
        );

        assertThrows(ValidationException.class, () -> controller.create(film2));
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
                LocalDate.of(1985, 7, 3),
                Duration.ofMinutes(116)
        );

        controller.getFilms().put(film1.getName(), film1);
    }
}