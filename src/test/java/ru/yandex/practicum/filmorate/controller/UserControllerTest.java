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

    /*@Test
    void findAll() {
    }*/

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

    /*@Test
    void put() {
    }*/

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