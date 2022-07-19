package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    /*
     * Создайте классы InMemoryFilmStorage и InMemoryUserStorage, имплементирующие новые интерфейсы,
     * и перенесите туда всю логику хранения, обновления и поиска объектов.
     *
     * Добавьте к InMemoryFilmStorage и InMemoryUserStorage аннотацию @Component,
     * чтобы впоследствии пользоваться внедрением зависимостей и передавать хранилища сервисам.
     */

    @Getter
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public void create(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void put(User user) {
        users.put(user.getId(), user);
    }
}
