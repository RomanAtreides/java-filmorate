package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserStorage {
    /*
     * Архитектура.
     * Создайте интерфейсы FilmStorage и UserStorage,
     * в которых будут определены методы добавления, удаления и модификации объектов.
     */

    Map<Long, User> getUsers();

    User findUserById(Long userId);

    Collection<User> findAll();

    User put(User user);

    List<User> findUserFriends(User user);
}
