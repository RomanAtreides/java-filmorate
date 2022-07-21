package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

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
    public void create(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void put(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public Collection<User> findUserFriends(Long userId) {
        List<User> userFriends = new ArrayList<>();
        User user = users.get(userId);

        for (Long friendId : user.getFriends()) {
            if (users.containsKey(userId)) {
                userFriends.add(users.get(friendId));
            }
        }
        return userFriends;
    }

    @Override
    public List<User> findCommonFriends(User user, User other) {
        List<User> commonFriends = new ArrayList<>();
        Set<Long> userFriends = user.getFriends();
        Set<Long> otherFriends = other.getFriends();

        if (userFriends == null || otherFriends == null) {
            return commonFriends;
        }
        
        for (Long userFriend : userFriends) {
            if (otherFriends.contains(userFriend)) {
                commonFriends.add(users.get(userFriend));
            }
        }
        return commonFriends;
    }
}
