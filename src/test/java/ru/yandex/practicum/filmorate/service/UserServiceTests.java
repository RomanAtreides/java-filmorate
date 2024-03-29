package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTests {
    private final UserService userService;

    @BeforeEach
    void setUp() {
        createTestUsers();
    }

    @Test
    void shouldThrowExceptionIfEmailIsBlank() {
        // Пользователь с пустой почтой
        User user3 = new User(
                3,
                "",
                "u3",
                LocalDate.of(1986, 6, 6),
                "user3 name",
                new Friendship(0, 0)
        );

        assertThrows(ValidationException.class, () -> userService.validate(user3));
        assertTrue(user3.getEmail().isBlank());
    }

    @Test
    void shouldThrowExceptionIfEmailNotContainsAtSymbol() {
        // Пользователь с почтой, которая не содержит символ @
        User user3 = new User(
                3,
                "user3email.com",
                "u3",
                LocalDate.of(1986, 6, 6),
                "user3 name",
                new Friendship(0, 0)
        );

        assertThrows(ValidationException.class, () -> userService.validate(user3));
        assertFalse(user3.getEmail().contains("@"));
    }

    @Test
    void shouldThrowExceptionIfLoginIsBlank() {
        // Пользователь с пустым логином
        User user3 = new User(
                3,
                "user3@email.com",
                "",
                LocalDate.of(1986, 6, 6),
                "user3 name",
                new Friendship(0, 0)
        );

        assertThrows(ValidationException.class, () -> userService.validate(user3));
        assertTrue(user3.getLogin().isBlank());
    }

    @Test
    void shouldThrowExceptionIfLoginContainsSpaces() {
        // Пользователь с 1 пробелом в логине
        User user3 = new User(
                3,
                "user3@email.com",
                "u 3",
                LocalDate.of(1986, 6, 6),
                "user3 name",
                new Friendship(0, 0)
        );

        // Пользователь с 2 пробелами в логине
        User user4 = new User(
                4,
                "user4@email.com",
                "u  4",
                LocalDate.of(1987, 7, 7),
                "user4 name",
                new Friendship(0, 0)
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
                3,
                "user3@email.com",
                "u3",
                LocalDate.of(1986, 6, 6),
                "",
                new Friendship(0, 0)
        );

        assertTrue(user3.getName().isBlank());
        userService.create(user3);
        assertEquals(user3.getLogin(), user3.getName());
    }

    @Test
    void shouldThrowExceptionIfBirthdayIsAfterNow() {
        // Пользователь с датой рождения, которая позже сегодняшнего дня
        User user3 = new User(
                3,
                "user3@email.com",
                "u3",
                LocalDate.now().plusDays(1),
                "user3 name",
                new Friendship(0, 0)
        );

        assertThrows(ValidationException.class, () -> userService.validate(user3));
        assertTrue(user3.getBirthday().isAfter(LocalDate.now()));
    }

    private void createTestUsers() {
        User user1 = new User(
                1,
                "user1@email.com",
                "u1",
                LocalDate.of(1984, 4, 4),
                "user1 name",
                new Friendship(0, 0)
        );

        User user2 = new User(
                2,
                "user2@email.com",
                "u2",
                LocalDate.of(1985, 5, 5),
                "user2 name",
                new Friendship(0, 0)
        );

        userService.create(user1);
        userService.create(user2);
    }
}
