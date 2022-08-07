package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
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

/*public long saveAndReturnId(User user) {
    String sqlQuery = "INSERT INTO users(first_name, last_name, yearly_income) VALUES (?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
    PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
    stmt.setString(1, employee.getFirstName());
    stmt.setString(2, employee.getLastName());
    stmt.setLong(3, employee.getYearlyIncome());
    return stmt;
    }, keyHolder);
    return keyHolder.getKey().longValue();
}*/

/*@Override
public User save(User user) {
    String sqlQuery = "insert into USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) values (?, ?, ?, ?)";

    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
        PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
        stmt.setString(1, user.getEmailNew());
        stmt.setString(2, user.getLogin());
        stmt.setString(3, user.getName());
        final LocalDate birthday = user.getBirthday();
        if (birthday == null) {
            stmt.setNull(4, Types.DATE);
        } else {
            stmt.setDate(4, Date.valueOf(birthday));
        }
        return stmt;
    }, keyHolder);
    user.setId(keyHolder.getKey().longValue());
    return user;
}*/

@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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
