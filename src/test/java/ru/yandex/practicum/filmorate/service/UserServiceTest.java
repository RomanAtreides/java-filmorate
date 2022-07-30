package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserController controller;
    UserStorage userStorage = new InMemoryUserStorage();
    UserService userService = new UserService(userStorage);

    @BeforeEach
    void setUp() {
        controller = new UserController(userService);
        createTestUsers();
    }

    @Test
    void shouldThrowExceptionIfEmailIsBlank() {
        // Пользователь с пустой почтой
        User user3 = new User(
                "",
                "u3",
                LocalDate.of(1986, 6, 6),
                "user3 name"
        );

        assertThrows(ValidationException.class, () -> userService.validate(user3));
        assertTrue(user3.getEmail().isBlank());
    }

    @Test
    void shouldThrowExceptionIfEmailNotContainsAtSymbol() {
        // Пользователь с почтой, которая не содержит символ @
        User user3 = new User(
                "user3email.com",
                "u3",
                LocalDate.of(1986, 6, 6),
                "user3 name"
        );

        assertThrows(ValidationException.class, () -> userService.validate(user3));
        assertFalse(user3.getEmail().contains("@"));
    }

    @Test
    void shouldThrowExceptionIfLoginIsBlank() {
        // Пользователь с пустым логином
        User user3 = new User(
                "user3@email.com",
                "",
                LocalDate.of(1986, 6, 6),
                "user3 name"
        );

        assertThrows(ValidationException.class, () -> userService.validate(user3));
        assertTrue(user3.getLogin().isBlank());
    }

    @Test
    void shouldThrowExceptionIfLoginContainsSpaces() {
        // Пользователь с 1 пробелом в логине
        User user3 = new User(
                "user3@email.com",
                "u 3",
                LocalDate.of(1986, 6, 6),
                "user3 name"
        );

        // Пользователь с 2 пробелами в логине
        User user4 = new User(
                "user4@email.com",
                "u  4",
                LocalDate.of(1987, 7, 7),
                "user4 name"
        );

        int user3LoginLinesNumber = user3.getLogin().split(" ").length;
        int user4LoginLinesNumber = user4.getLogin().split(" ").length;

        assertTrue(user3LoginLinesNumber > 1);
        assertTrue(user4LoginLinesNumber > 1);
        assertThrows(ValidationException.class, () -> userService.validate(user3));
        assertThrows(ValidationException.class, () -> userService.validate(user4));
    }

    @Test
    void shouldReplaceNameWithLoginIfNameIsBlank() {
        // Пользователь с пустым именем
        User user3 = new User(
                "user3@email.com",
                "u3",
                LocalDate.of(1986, 6, 6),
                ""
        );

        assertTrue(user3.getName().isBlank());
        controller.create(user3);
        assertEquals(user3.getLogin(), user3.getName());
    }

    @Test
    void shouldThrowExceptionIfBirthdayIsAfterNow() {
        // Пользователь с датой рождения, которая позже сегодняшнего дня
        User user3 = new User(
                "user3@email.com",
                "u3",
                LocalDate.now().plusDays(1),
                "user3 name"
        );

        assertThrows(ValidationException.class, () -> userService.validate(user3));
        assertTrue(user3.getBirthday().isAfter(LocalDate.now()));
    }

    private void createTestUsers() {
        User user1 = new User(
                "user1@email.com",
                "u1",
                LocalDate.of(1984, 4, 4),
                "user1 name"
        );

        User user2 = new User(
                "user2@email.com",
                "u2",
                LocalDate.of(1985, 5, 5),
                "user2 name"
        );

        controller.create(user1);
        controller.create(user2);
    }
}