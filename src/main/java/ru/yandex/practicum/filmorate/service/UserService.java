package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;
    private long userId = 0;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    private long generateUserId() {
        return ++userId;
    }

    public User findUserById(Long userId) {
        User user = userStorage.findUserById(userId);

        validate(user);
        return user;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        validate(user);
        user.setId(generateUserId());
        log.info("Пользователь \"{}\" добавлен в базу", user.getName());
        return userStorage.create(user);
    }

    public User put(User user) {
        validate(user);
        log.info("Данные пользователя \"{}\" обновлены", user.getName());
        return userStorage.put(user);
    }

    public User addFriend(Long userId, Long friendId) {
        if (userId < 1 || friendId < 1) {
            throw new UserNotFoundException("id не может быть отрицательным!");
        }
        friendshipStorage.addFriend(userId, friendId);
        return userStorage.findUserById(friendId);
    }

    public User removeFriend(Long userId, Long friendId) {
        User user = findUserById(userId);
        User friend = findUserById(friendId);

        friendshipStorage.removeFriend(user, friend);
        return friend;
    }

    public List<User> findUserFriends(Long userId) {
        User user = findUserById(userId);
        return userStorage.findUserFriends(user);
    }

    public List<User> findCommonFriends(Long userId, Long otherId) {
        User user = findUserById(userId);
        User other = findUserById(otherId);

        return userStorage.findCommonFriends(user, other);
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
