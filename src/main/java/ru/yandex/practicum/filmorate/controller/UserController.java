package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserValidationService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Getter
    private final Map<Integer, User> users = new HashMap<>();
    private final UserValidationService userValidationService = new UserValidationService();
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
        if (userValidationService.validate(user)) {
            generateUserId(user);
            users.put(user.getId(), user);
            log.info("Пользователь {} добавлен в базу", user.getName());
        }
        return user;
    }

    // Обновление пользователя
    @PutMapping
    public User put(@RequestBody User user) {
        if (userValidationService.validate(user)) {
            users.put(user.getId(), user);
            log.info("Данные пользователя {} обновлены", user.getName());
        }
        return user;
    }
}
