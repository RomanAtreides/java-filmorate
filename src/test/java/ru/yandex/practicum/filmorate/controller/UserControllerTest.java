package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController controller;

    @BeforeEach
    void setUp() {
        controller = new UserController();
        createTestUsers();
    }

    @Test
    void shouldThrowExceptionIfEmailAlreadyExists() {
        // Пользователь с почтой, которая уже есть в базе
        User user2 = new User(
                2,
                "user1@email.com",
                "u2",
                LocalDate.of(1986, 6, 6),
                "user2 name"
        );

        assertThrows(ValidationException.class, () -> controller.create(user2));
    }

    @Test
    void shouldThrowExceptionIfEmailIsBlank() {
        // Пользователь с пустой почтой
        User user2 = new User(
                2,
                "",
                "u2",
                LocalDate.of(1986, 6, 6),
                "user2 name"
        );

        assertThrows(ValidationException.class, () -> controller.validate(user2));
        assertTrue(user2.getEmail().isBlank());
    }

    @Test
    void shouldThrowExceptionIfEmailNotContainsAtSymbol() {
        // Пользователь с почтой, которая не содержит символ @
        User user2 = new User(
                2,
                "user2email.com",
                "u2",
                LocalDate.of(1986, 6, 6),
                "user2 name"
        );

        assertThrows(ValidationException.class, () -> controller.validate(user2));
        assertFalse(user2.getEmail().contains("@"));
    }

    @Test
    void shouldThrowExceptionIfLoginIsBlank() {
        // Пользователь с пустым логином
        User user2 = new User(
                2,
                "user2@email.com",
                "",
                LocalDate.of(1986, 6, 6),
                "user2 name"
        );

        assertThrows(ValidationException.class, () -> controller.validate(user2));
        assertTrue(user2.getLogin().isBlank());
    }

    @Test
    void shouldThrowExceptionIfLoginContainsSpaces() {
        // Пользователь с 1 пробелом в логине
        User user2 = new User(
                2,
                "user2@email.com",
                "u 2",
                LocalDate.of(1986, 6, 6),
                "user2 name"
        );

        // Пользователь с 2 пробелами в логине
        User user3 = new User(
                3,
                "user3@email.com",
                "u  3",
                LocalDate.of(1987, 7, 7),
                "user3 name"
        );

        int user2LoginLinesNumber = user2.getLogin().split(" ").length;
        int user3LoginLinesNumber = user3.getLogin().split(" ").length;

        assertTrue(user2LoginLinesNumber > 1);
        assertTrue(user3LoginLinesNumber > 1);
        assertThrows(ValidationException.class, () -> controller.validate(user2));
        assertThrows(ValidationException.class, () -> controller.validate(user3));
    }

    @Test
    void shouldReplaceNameWithLoginIfNameIsNull() {
        // Пользователь со значением null вместо имени
        User user2 = new User(
                2,
                "user2@email.com",
                "u2",
                LocalDate.of(1986, 6, 6),
                null
        );

        assertNull(user2.getName());
        controller.validate(user2);
        assertEquals(user2.getLogin(), user2.getName());
    }

    @Test
    void shouldReplaceNameWithLoginIfNameIsBlank() {
        // Пользователь с пустым именем
        User user2 = new User(
                2,
                "user2@email.com",
                "u2",
                LocalDate.of(1986, 6, 6),
                ""
        );

        assertTrue(user2.getName().isBlank());
        controller.validate(user2);
        assertEquals(user2.getLogin(), user2.getName());
    }

    @Test
    void shouldThrowExceptionIfBirthdayIsAfterNow() {
        // Пользователь с датой рождения, которая позже сегодняшнего дня
        User user2 = new User(
                2,
                "user2@email.com",
                "u2",
                LocalDate.now().plusDays(1),
                "user2 name"
        );

        assertThrows(ValidationException.class, () -> controller.validate(user2));
        assertTrue(user2.getBirthday().isAfter(LocalDate.now()));
    }

    private void createTestUsers() {
        User user1 = new User(
                1,
                "user1@email.com",
                "u1",
                LocalDate.of(1985, 5, 5),
                "user1 name"
        );

        controller.getUsers().put(user1.getEmail(), user1);
    }
}