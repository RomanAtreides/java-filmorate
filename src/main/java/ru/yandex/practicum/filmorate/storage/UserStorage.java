package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    Map<Long, User> getUsers();

    User findUserById(Long userId);

    Collection<User> findAll();

    User create(User user);

    User put(User user);
}
