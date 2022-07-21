package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    /*
     * Архитектура.
     * Создайте интерфейсы FilmStorage и UserStorage,
     * в которых будут определены методы добавления, удаления и модификации объектов.
     */

    User findUserById(Long userId);

    Collection<User> findAll();

    void create(User user);

    void put(User user);

    List<User> findUserFriends(User user);

    List<User> findCommonFriends(User user, User other);
}
