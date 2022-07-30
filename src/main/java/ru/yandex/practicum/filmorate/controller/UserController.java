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
        return userService.findUserById(userId);
    }

    // Получение списка всех пользователей
    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    // Создание пользователя
    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    // Обновление существующего в базе пользователя
    @PutMapping
    public User put(@RequestBody User user) {
         return userService.put(user);
    }

    // Добавление в список друзей
    @PutMapping("/{userId}/friends/{friendId}")
    public User addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        return userService.addFriend(userId, friendId);
    }

    // Удаление из друзей
    @DeleteMapping("/{userId}/friends/{friendId}")
    public User removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        return userService.removeFriend(userId, friendId);
    }

    // Получение списка друзей пользователя
    @GetMapping("/{userId}/friends")
    public List<User> findUserFriends(@PathVariable Long userId) {
        return userService.findUserFriends(userId);
    }

    // Получение списка общих друзей 2 пользователей
    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable Long userId, @PathVariable Long otherId) {
        return userService.findCommonFriends(userId, otherId);
    }
}
