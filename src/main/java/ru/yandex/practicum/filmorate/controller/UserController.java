package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.UserValidationService;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    /*
     * Зависимости.
     * Переделайте код в контроллерах, сервисах и хранилищах под использование внедрения зависимостей.
     *
     * Используйте аннотации @Service, @Component, @Autowired. Внедряйте зависимости через конструкторы классов.
     *
     * Классы-сервисы должны иметь доступ к классам-хранилищам.
     * Убедитесь, что сервисы зависят от интерфейсов классов-хранилищ, а не их реализаций.
     * Таким образом в будущем будет проще добавлять и использовать новые реализации с другим типом хранения данных.
     *
     * Сервисы должны быть внедрены в соответствующие контроллеры.
     */

    private final UserValidationService userValidationService;
    private final UserService userService;

    public UserController(UserValidationService userValidationService, UserService userService) {
        this.userValidationService = userValidationService;
        this.userService = userService;
    }

    // Получение пользователя по идентификатору
    @GetMapping("/{userId}")
    public User findUserById(@PathVariable Long userId) {
        User user = userService.findUserById(userId);

        userValidationService.validate(user);
        return user;
    }

    // Получение списка всех пользователей
    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    // Создание пользователя
    @PostMapping
    public User create(@RequestBody User user) {
        if (userValidationService.validate(user)) {
            userService.create(user);
        }
        return user;
    }

    // Обновление существующего в базе пользователя
    @PutMapping
    public User put(@RequestBody User user) {
        if (userValidationService.validate(user)) {
            userService.put(user);
        }
        return user;
    }

    // Добавление в список друзей
    @PutMapping("/{userId}/friends/{friendId}")
    public User addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        User user = userService.findUserById(userId);
        User friend = userService.findUserById(friendId);

        if (userValidationService.validate(user) && userValidationService.validate(friend)) {
            userService.addFriend(user, friend);
        }
        return friend;
    }

    // Удаление из друзей
    @DeleteMapping("/{userId}/friends/{friendId}")
    public User removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        User user = userService.findUserById(userId);
        User friend = userService.findUserById(friendId);

        if (userValidationService.validate(user) && userValidationService.validate(friend)) {
            userService.removeFriend(user, friend);
        }
        return friend;
    }

    // Получение списка друзей пользователя
    @GetMapping("/{userId}/friends")
    public Collection<User> findFriends(@PathVariable Long userId) {
        User user = userService.findUserById(userId);

        userValidationService.validate(user);
        return userService.findFriends(userId);
    }

    //todo:
    // список друзей, общих с другим пользователем
    // GET /users/{id}/friends/common/{otherId}
    @GetMapping("/{userId}/friends/common/{otherId}")
    public Collection<User> friendsList(@PathVariable Long userId, @PathVariable Long otherId) {
        return new ArrayList<>();
    }
}
