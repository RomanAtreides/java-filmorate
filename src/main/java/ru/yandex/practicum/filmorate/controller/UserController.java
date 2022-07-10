package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    @Getter
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 0;

    private void generateUserId(User user) {
        user.setId(++userId);
    }

    // Получение списка всех пользователей
    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    // Создание пользователя
    @PostMapping
    public User create(@RequestBody User user) {
        if (validate(user)) {
            generateUserId(user);
            users.put(user.getId(), user);
            log.info("Пользователь {} добавлен в базу", user.getName());
        }
        return user;
    }

    // Обновление пользователя
    @PutMapping
    public User put(@RequestBody User user) {
        if (validate(user)) {
            users.put(user.getId(), user);
            log.info("Данные пользователя {} обновлены", user.getName());
        }
        return user;
    }

    public boolean validate(User user) {
        boolean isValid = true;
        int loginLinesNumber = user.getLogin().split(" ").length;

        if (user.getId() < 0) {
            log.warn("Попытка добавить пользователя с отрицательным id {}", user.getId());
            throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "id не может быть отрицательным!");
        }

        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Попытка добавить пользователя с неверно указанной почтой - {}", user.getEmail());
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Не указана электронная почта!");
        }

        if (user.getLogin().isBlank() || loginLinesNumber > 1) {
            log.warn("Попытка добавить пользователя с неверным логином - {}", user.getLogin());
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Логин указан неверно!");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Попытка добавить пользователя с неверной датой рождения - {}", user.getBirthday());
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Дата рождения указана неверно!");
        }

        if (user.getName().isBlank()) {
            log.info("Попытка добавить пользователя с пустым именем. Имя будет заменено на логин - {}",
                    user.getLogin());
            user.setName(user.getLogin());
        }
        return isValid;
    }
}
