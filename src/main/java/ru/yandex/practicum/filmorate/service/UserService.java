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
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private long userId = 0;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
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
        User user = findUserById(userId);
        User friend = findUserById(friendId);

        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
        return friend;
    }

    public User removeFriend(Long userId, Long friendId) {
        User user = findUserById(userId);
        User friend = findUserById(friendId);

        Set<Long> userFriends = user.getFriends();
        Set<Long> friendFriends = friend.getFriends();

        boolean isNotFriends = userFriends == null || !userFriends.contains(friend.getId()) &&
                friendFriends == null || !friendFriends.contains(user.getId());

        if (isNotFriends) {
            throw new UserNotFoundException("У пользователя нет такого друга!");
        }
        userFriends.remove(friend.getId());
        friendFriends.remove(user.getId());
        return friend;
    }

    public List<User> findUserFriends(Long userId) {
        List<User> userFriends = new ArrayList<>();
        User user = findUserById(userId);

        for (Long friendId : user.getFriends()) {
            User friend = findUserById(friendId);

            if (friend != null) {
                userFriends.add(friend);
            }
        }
        return userFriends;
    }

    public List<User> findCommonFriends(Long userId, Long otherId) {
        User user = findUserById(userId);
        User other = findUserById(otherId);

        Set<Long> userFriends = user.getFriends();
        Set<Long> otherFriends = other.getFriends();

        return userFriends.stream()
                .filter(u -> otherFriends.stream().anyMatch(o -> o.equals(u)))
                .map(userStorage::findUserById)
                .collect(Collectors.toList());
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
