package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.UserValidationService;

import java.util.Collection;
import java.util.List;

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
        userValidationService.validate(user);
        return userService.create(user);
    }

    // Обновление существующего в базе пользователя
    @PutMapping
    public User put(@RequestBody User user) {
        userValidationService.validate(user);
         return userService.put(user);
    }

    // Добавление в список друзей
    @PutMapping("/{userId}/friends/{friendId}")
    public User addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        User user = userService.findUserById(userId);
        User friend = userService.findUserById(friendId);

        userValidationService.validate(user);
        userValidationService.validate(friend);
        userService.addFriend(user, friend);
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
    public List<User> findUserFriends(@PathVariable Long userId) {
        User user = userService.findUserById(userId);

        userValidationService.validate(user);
        return userService.findUserFriends(user);
    }

    // Получение списка общих друзей 2 пользователей
    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable Long userId, @PathVariable Long otherId) {
        User user = userService.findUserById(userId);
        User other = userService.findUserById(otherId);

        userValidationService.validate(user);
        userValidationService.validate(other);
        return userService.findCommonFriends(user, other);
    }
}
