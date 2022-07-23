package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private long userId = 0;

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
        user.getFriends().add(friend.getId());
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

        for (Long userFriend : userFriends) {
            if (otherFriends.contains(userFriend)) {
                commonFriends.add(userStorage.getUsers().get(userFriend));
            }
        }
        return commonFriends;
    }

    public void validate(User user) {
        if (user == null) {
            log.warn("Попытка получить пользователя по несуществующему id");
            throw new UserNotFoundException("Пользователь с таким id не найден!");
        }

        if (user.getId() < 0) {
            log.warn("Попытка добавить пользователя с отрицательным id ({})", user.getId());
            throw new UserNotFoundException("id не может быть отрицательным!");
        }

        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Попытка добавить пользователя с неверно указанной почтой \"{}\"", user.getEmail());
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Не указана электронная почта!");
        }

        int loginLinesNumber = user.getLogin().split(" ").length;

        if (user.getLogin().isBlank() || loginLinesNumber > 1) {
            log.warn("Попытка добавить пользователя с неверным логином \"{}\"", user.getLogin());
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Логин указан неверно!");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Попытка добавить пользователя с неверной датой рождения {}", user.getBirthday());
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Дата рождения указана неверно!");
        }

        if (user.getName().isBlank()) {
            log.info("Попытка добавить пользователя с пустым именем. В качестве имени будет установлен логин \"{}\"",
                    user.getLogin());
            user.setName(user.getLogin());
        }
    }
}
