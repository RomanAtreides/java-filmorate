package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User findUserById(Long userId);

    Collection<User> findAll();

    User create(User user);

    User put(User user);
}
