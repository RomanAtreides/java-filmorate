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
                "film2 description",
                LocalDate.of(1986, 7, 1),
                Duration.ofMinutes(110),
                "film1 name"
                );

        assertThrows(ValidationException.class, () -> controller.create(film2));
    }

    @Test
    void shouldThrowExceptionIfNameIsNullOrBlank() {
        Film film2 = new Film(
                2,
                "film2 description",
                LocalDate.of(1986, 7, 1),
                Duration.ofMinutes(110),
                "film1 name"
        );

        film2.setName(null);
        assertThrows(ValidationException.class, () -> controller.validate(film2));
        film2.setName("");
        assertThrows(ValidationException.class, () -> controller.validate(film2));
    }

    @Test
    void shouldThrowExceptionIfDescriptionIsLonger200() {
        Film film2 = new Film(
                2,
                "film2 description." +
                        "Marty McFly, a 17-year-old high school student, is accidentally sent thirty years" +
                        "into the past in a time-traveling DeLorean invented by his close friend," +
                        "the eccentric scientist Doc Brown",
                LocalDate.of(1986, 7, 1),
                Duration.ofMinutes(110),
                "film1 name"
        );

        System.out.println(film2.getDescription().length());
        assertThrows(ValidationException.class, () -> controller.validate(film2));
        assertTrue(film2.getDescription().length() > 200);
    }

    void createTestFilms() {
        Film film1 = new Film(
                1,
                "film1 description",
                LocalDate.of(1985, 7, 3),
                Duration.ofMinutes(116),
                "film1 name"
        );

        controller.getFilms().put(film1.getName(), film1);
    }
}