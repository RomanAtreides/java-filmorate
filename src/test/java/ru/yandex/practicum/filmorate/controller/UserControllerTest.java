package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
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
        User user2 = new User(
                2,
                "user1@email.com",
                "u2",
                LocalDate.of(1986, 6, 6)
        );

        assertThrows(ValidationException.class, () -> controller.create(user2));
    }

    @Test
    void shouldThrowExceptionIfEmailIsBlank() {
        User user2 = new User(
                2,
                "",
                "u2",
                LocalDate.of(1986, 6, 6)
        );

        assertThrows(ValidationException.class, () -> controller.validate(user2));
        assertTrue(user2.getEmail().isBlank());
    }

    @Test
    void shouldThrowExceptionIfEmailNotContainsAtSymbol() {
        User user2 = new User(
                2,
                "user1email.com",
                "u2",
                LocalDate.of(1986, 6, 6)
        );

        assertThrows(ValidationException.class, () -> controller.validate(user2));
        assertFalse(user2.getEmail().contains("@"));
    }

    @Test
    void shouldThrowExceptionIfLoginIsBlank() {
        User user2 = new User(
                2,
                "user1@email.com",
                "",
                LocalDate.of(1986, 6, 6)
        );

        assertThrows(ValidationException.class, () -> controller.validate(user2));
        assertTrue(user2.getLogin().isBlank());
    }

    @Test
    void shouldThrowExceptionIfLoginContainsSpaces() {
        User user2 = new User(
                2,
                "user1@email.com",
                "u 2",
                LocalDate.of(1986, 6, 6)
        );

        assertThrows(ValidationException.class, () -> controller.validate(user2));
        assertTrue(user2.getLogin().contains(" "));
    }

    @Test
    void shouldReplaceNameWithLoginIfNameIsNull() {
        User user2 = new User(
                2,
                "user1@email.com",
                "u2",
                LocalDate.of(1986, 6, 6)
        );

        assertNull(user2.getName());
        controller.validate(user2);
        assertEquals(user2.getLogin(), user2.getName());
    }

    @Test
    void shouldReplaceNameWithLoginIfNameIsBlank() {
        User user2 = new User(
                2,
                "user1@email.com",
                "u2",
                LocalDate.of(1986, 6, 6)
        );

        user2.setName("");
        assertTrue(user2.getName().isBlank());
        controller.validate(user2);
        assertEquals(user2.getLogin(), user2.getName());
    }

    @Test
    void shouldThrowExceptionIfBirthdayIsAfterNow() {
        User user2 = new User(
                2,
                "user1@email.com",
                "u2",
                LocalDate.now().plusDays(1)
        );

        assertThrows(ValidationException.class, () -> controller.validate(user2));
        assertTrue(user2.getBirthday().isAfter(LocalDate.now()));
    }

    private void createTestUsers() {
        User user1 = new User(
                1,
                "user1@email.com",
                "u1",
                LocalDate.of(1985, 5, 5)
        );

        controller.getUsers().put(user1.getEmail(), user1);
    }
}