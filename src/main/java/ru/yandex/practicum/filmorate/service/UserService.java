package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {
    /*
     * Новая логика.
     * Создайте UserService, который будет отвечать за такие операции с пользователями,
     * как добавление в друзья,
     * удаление из друзей,
     * вывод списка общих друзей.
     * Пока пользователям не надо одобрять заявки в друзья — добавляем сразу.
     * То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.
     *
     * Добавьте к ним аннотацию @Service — тогда к ним можно будет получить доступ из контроллера.
     */

    /*
     * Подсказка: @Service vs @Component.
     * @Component — аннотация, которая определяет класс как управляемый Spring.
     * Такой класс будет добавлен в контекст приложения при сканировании.
     * @Service не отличается по поведению, но обозначает более узкий спектр классов — такие,
     * которые содержат в себе бизнес-логику и, как правило, не хранят состояние.
     */

    private final UserStorage userStorage;
    private int userId = 0;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private void generateUserId(User user) {
        user.setId(++userId);
    }

    public User findUserById(Long userId) {
        return userStorage.findUserById(userId);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        generateUserId(user);
        log.info("Пользователь \"{}\" добавлен в базу", user.getName());
        return userStorage.put(user);
    }

    public User put(User user) {
        log.info("Данные пользователя \"{}\" обновлены", user.getName());
        return userStorage.put(user);
    }

    public void addFriend(User user, User friend) {
        if (user.getFriends() == null) {
            user.setFriends(new LinkedHashSet<>());
        }
        user.getFriends().add(friend.getId());

        if (friend.getFriends() == null) {
            friend.setFriends(new LinkedHashSet<>());
        }
        friend.getFriends().add(user.getId());
    }

    public void removeFriend(User user, User friend) {
        Set<Long> userFriends = user.getFriends();
        Set<Long> friendFriends = friend.getFriends();

        boolean isNotFriends = userFriends == null || !userFriends.contains(friend.getId()) &&
                friendFriends == null || !friendFriends.contains(user.getId());

        if (isNotFriends) {
            throw new UserNotFoundException("У пользователя нет такого друга!");
        }
        userFriends.remove(friend.getId());
        friendFriends.remove(user.getId());
    }

    public List<User> findUserFriends(User user) {
        List<User> userFriends = new ArrayList<>();
        Map<Long, User> users = userStorage.getUsers();

        for (Long friendId : user.getFriends()) {
            if (users.containsKey(user.getId())) {
                userFriends.add(users.get(friendId));
            }
        }

        return userFriends;
    }

    public List<User> findCommonFriends(User user, User other) {
        List<User> commonFriends = new ArrayList<>();
        Set<Long> userFriends = user.getFriends();
        Set<Long> otherFriends = other.getFriends();

        if (userFriends == null || otherFriends == null) {
            return commonFriends;
        }

        for (Long userFriend : userFriends) {
            if (otherFriends.contains(userFriend)) {
                commonFriends.add(userStorage.getUsers().get(userFriend));
            }
        }
        return commonFriends;
    }
}
