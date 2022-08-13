package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {

    User findUserById(Long userId);

    Collection<User> findAll();

    List<User> findUserFriends(User user);

    List<User> findCommonFriends(User user, User other);

    User create(User user);

    User put(User user);
}
