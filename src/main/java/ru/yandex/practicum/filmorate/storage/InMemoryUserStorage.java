package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User findUserById(Long userId) {
        return users.get(userId);
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public List<User> findUserFriends(User user) {
        return new ArrayList<>();
    }

    @Override
    public List<User> findCommonFriends(User user, User other) {
        return null;
    }

    @Override
    public User create(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User put(User user) {
        users.put(user.getId(), user);
        return user;
    }
}
