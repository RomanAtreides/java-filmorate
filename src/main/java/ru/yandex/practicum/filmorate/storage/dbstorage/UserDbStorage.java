package ru.yandex.practicum.filmorate.storage.dbstorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Repository
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User findUserById(Long userId) {
        String sqlQuery = "SELECT user_id, email, login, birthday, user_name FROM users WHERE user_id = ?";

        return jdbcTemplate.query(sqlQuery, new UserMapper(), userId).stream()
                .findAny()
                .orElse(null);
    }

    @Override
    public Collection<User> findAll() {
        String sqlQuery = "SELECT user_id, email, login, birthday, user_name FROM users";
        return jdbcTemplate.query(sqlQuery, new UserMapper());
    }

    public List<User> findUserFriends(User user) {
        String sqlQuery = "SELECT user_id, email, login, birthday, user_name " +
                "FROM users " +
                "WHERE user_id IN (SELECT friend_id FROM friendship WHERE user_id = ?)";
        return jdbcTemplate.query(sqlQuery, new UserMapper(), user.getId());
    }

    @Override
    public List<User> findCommonFriends(User user, User other) {
        String sqlQuery = "SELECT * " +
                "FROM (SELECT user_id, email, login, birthday, user_name " +
                "FROM users " +
                "WHERE user_id IN (SELECT friend_id " +
                "FROM friendship " +
                "WHERE user_id = ?) " +
                "UNION ALL SELECT user_id, email, login, birthday, user_name " +
                "FROM users " +
                "WHERE user_id IN (SELECT friend_id " +
                "FROM friendship " +
                "WHERE user_id = ?) ) " +
                "GROUP BY user_id " +
                "HAVING COUNT(user_id) > 1";

        return jdbcTemplate.query(sqlQuery, new UserMapper(), user.getId(), other.getId());
    }

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
        return user;
    }

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
        return user;
    }
}
