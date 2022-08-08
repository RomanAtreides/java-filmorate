package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private User createUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(
                resultSet.getLong("user_id"),
                resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getDate("birthday").toLocalDate(),
                resultSet.getString("user_name")
        );
    }

    /*@Override
    public User findUserById(Long userId) {
        return users.get(userId);
    }*/
    @Override
    public User findUserById(Long userId) {
        String sqlQuery = "SELECT user_id, email, login, birthday, user_name FROM users WHERE user_id = ?";

        User user = jdbcTemplate.query(sqlQuery, this::createUser, userId).stream()
                .findAny()
                .orElse(null);

        return user;
        //return users.get(userId);
    }

    /*@Override
    public Collection<User> findAll() {
        Collection<User> allUsers = users.values();
        return allUsers;
    }*/
    @Override
    public Collection<User> findAll() {
        String sqlQuery = "SELECT user_id, email, login, birthday, user_name FROM users";
        Collection<User> allUsers = jdbcTemplate.query(sqlQuery, this::createUser);
        return allUsers;
    }

    /*@Override
    public User create(User user) {
         users.put(user.getId(), user);
         return user;
    }*/
    @Override
    public User create(User user) {
        String sqlQuery = "INSERT INTO users (email, login, birthday, user_name) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"user_id"});

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getLogin());

            final LocalDate birthday = user.getBirthday();

            if (birthday == null) {
                statement.setNull(3, Types.DATE);
            } else {
                statement.setDate(3, Date.valueOf(birthday));
            }
            statement.setString(4, user.getName());
            return statement;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        users.put(user.getId(), user); // The line from the old implementation, should be deleted
        return user;
    }

    /*@Override
    public User put(User user) {
        users.put(user.getId(), user);
        return user;
    }*/
    @Override
    public User put(User user) {
        String sqlQuery = "UPDATE users SET email = ?, login = ?, birthday = ?, user_name = ? WHERE user_id = ?";

        jdbcTemplate.update(
                sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getName(),
                user.getId()
        );

        users.put(user.getId(), user); // The line from the old implementation, should be deleted
        return user;
    }
}
