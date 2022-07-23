package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Получение пользователя по идентификатору
    @GetMapping("/{userId}")
    public User findUserById(@PathVariable Long userId) {
        User user = userService.findUserById(userId);

        userService.validate(user);
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
        userService.validate(user);
        return userService.create(user);
    }

    // Обновление существующего в базе пользователя
    @PutMapping
    public User put(@RequestBody User user) {
        userService.validate(user);
         return userService.put(user);
    }

    // Добавление в список друзей
    @PutMapping("/{userId}/friends/{friendId}")
    public User addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        User user = userService.findUserById(userId);
        User friend = userService.findUserById(friendId);

        userService.validate(user);
        userService.validate(friend);
        userService.addFriend(user, friend);
        return friend;
    }

    // Удаление из друзей
    @DeleteMapping("/{userId}/friends/{friendId}")
    public User removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        User user = userService.findUserById(userId);
        User friend = userService.findUserById(friendId);

        userService.validate(user);
        userService.validate(friend);
        userService.removeFriend(user, friend);
        return friend;
    }

    // Получение списка друзей пользователя
    @GetMapping("/{userId}/friends")
    public List<User> findUserFriends(@PathVariable Long userId) {
        User user = userService.findUserById(userId);

        userService.validate(user);
        return userService.findUserFriends(user);
    }

    // Получение списка общих друзей 2 пользователей
    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable Long userId, @PathVariable Long otherId) {
        User user = userService.findUserById(userId);
        User other = userService.findUserById(otherId);

        userService.validate(user);
        userService.validate(other);
        return userService.findCommonFriends(user, other);
    }
}
