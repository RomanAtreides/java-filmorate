package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/*
 * 1. Вам пригодятся созданные ранее интерфейсы UserStorage и FilmStorage.
 * Напишите для них новую имплементацию — например, UserDbStorage и FilmDbStorage.
 * Эти классы будут DAO — объектами доступа к данным.
 *
 * 2. Напишите в DAO соответствующие мапперы и методы,
 * позволяющие сохранять пользователей и фильмы в базу данных и получать их из неё.
 */

@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
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
