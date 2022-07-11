package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidationServiceTest {
    FilmController controller;
    FilmValidationService filmValidationService = new FilmValidationService();

    @BeforeEach
    void setUp() {
        controller = new FilmController();
        createTestFilms();
    }

    @Test
    void shouldThrowExceptionIfNameIsNullOrBlank() {
        // Фильм с пустым именем
        Film film3 = new Film(
                "",
                "film3 description",
                LocalDate.of(1986, 7, 1),
                110
        );

        // Фильм со значением null вместо имени
        Film film4 = new Film(
                null,
                "film4 description",
                LocalDate.of(1987, 8, 2),
                111
        );

        assertThrows(ValidationException.class, () -> filmValidationService.validate(film3));
        assertThrows(ValidationException.class, () -> filmValidationService.validate(film4));
    }

    @Test
    void shouldThrowExceptionIfDescriptionIsLonger200() {
        final String tooLongFilmDescription = "film3 description: Marty McFly," +
                "a 17-year-old high school student, is accidentally sent thirty years" +
                "into the past in a time-traveling DeLorean invented by his close friend," +
                "the eccentric scientist Doc Brown";

        // Фильм со слишком длинным описанием
        Film film3 = new Film(
                "film3 name",
                tooLongFilmDescription,
                LocalDate.of(1986, 7, 1),
                110
        );

        assertTrue(film3.getDescription().length() > 200);
        assertThrows(ValidationException.class, () -> filmValidationService.validate(film3));
    }

    @Test
    void shouldThrowExceptionIfReleaseDateIsBefore1895() {
        // Фильм с датой релиза ранее 28.12.1895
        Film film3 = new Film(
                "film3 name",
                "film3 description",
                LocalDate.of(1895, 12, 27),
                110
        );

        assertTrue(film3.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)));
        assertThrows(ValidationException.class, () -> filmValidationService.validate(film3));
    }

    @Test
    void shouldThrowExceptionIfDurationIsNotPositive() {
        // Фильм с нулевой продолжительностью
        Film film3 = new Film(
                "film3 name",
                "film3 description",
                LocalDate.of(1986, 6, 6),
                0
        );

        // Фильм с отрицательной продолжительностью
        Film film4 = new Film(
                "film4 name",
                "film4 description",
                LocalDate.of(1987, 7, 7),
                -1
        );

        // Фильм с положительной продолжительностью
        Film film5 = new Film(
                "film5 name",
                "film5 description",
                LocalDate.of(1988, 8, 8),
                1
        );

        assertThrows(ValidationException.class, () -> filmValidationService.validate(film3));
        assertThrows(ValidationException.class, () -> filmValidationService.validate(film4));
        assertDoesNotThrow(() -> filmValidationService.validate(film5));
    }

    void createTestFilms() {
        Film film1 = new Film(
                "film1 name",
                "film1 description",
                LocalDate.of(1985, 7, 3),
                116
        );

        Film film2 = new Film(
                "film2 name",
                "film2 description",
                LocalDate.of(1988, 1, 7),
                118
        );

        controller.create(film1);
        controller.create(film2);
    }
}