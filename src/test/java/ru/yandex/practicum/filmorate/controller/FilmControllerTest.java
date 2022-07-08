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
        // Фильм, имя которого совпадает с именем фильма уже добавленного в библиотеку
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
    void shouldThrowExceptionIfNameIsNullOrBlank() {
        // Фильм с пустым именем
        Film film2 = new Film(
                2,
                "",
                "film2 description",
                LocalDate.of(1986, 7, 1),
                Duration.ofMinutes(110)
        );

        // Фильм со значением null вместо имени
        Film film3 = new Film(
                3,
                null,
                "film3 description",
                LocalDate.of(1987, 8, 2),
                Duration.ofMinutes(111)
        );

        assertThrows(ValidationException.class, () -> controller.validate(film2));
        assertThrows(ValidationException.class, () -> controller.validate(film3));
    }

    @Test
    void shouldThrowExceptionIfDescriptionIsLonger200() {
        // Фильм со слишком длинным описанием
        Film film2 = new Film(
                2,
                "film2 name",
                "film2 description." +
                "Marty McFly, a 17-year-old high school student, is accidentally sent thirty years" +
                "into the past in a time-traveling DeLorean invented by his close friend," +
                "the eccentric scientist Doc Brown",
                LocalDate.of(1986, 7, 1),
                Duration.ofMinutes(110)
        );

        assertThrows(ValidationException.class, () -> controller.validate(film2));
        assertTrue(film2.getDescription().length() > 200);
    }

    @Test
    void shouldThrowExceptionIfReleaseDateIsBefore1895() {
        // Фильм с датой релиза ранее 28.12.1895
        Film film2 = new Film(
                2,
                "film2 name",
                "film2 description",
                LocalDate.of(1895, 12, 27),
                Duration.ofMinutes(110)
        );

        assertThrows(ValidationException.class, () -> controller.validate(film2));
        assertTrue(film2.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)));
    }

    @Test
    void shouldThrowExceptionIfDurationIsNotPositive() {
        // Фильм с нулевой продолжительностью
        Film film2 = new Film(
                2,
                "film2 name",
                "film2 description",
                LocalDate.of(1986, 6, 6),
                Duration.ofMinutes(0)
        );

        // Фильм с отрицательной продолжительностью
        Film film3 = new Film(
                3,
                "film3 name",
                "film3 description",
                LocalDate.of(1987, 7, 7),
                Duration.ofMinutes(-1)
        );

        // Фильм с положительной продолжительностью
        Film film4 = new Film(
                4,
                "film4 name",
                "film4 description",
                LocalDate.of(1988, 8, 8),
                Duration.ofMinutes(1)
        );

        assertThrows(ValidationException.class, () -> controller.validate(film2));
        assertThrows(ValidationException.class, () -> controller.validate(film3));
        assertDoesNotThrow(() -> controller.validate(film4));
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