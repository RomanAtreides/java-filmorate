package ru.yandex.practicum.filmorate.storage.dbstorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Repository
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(Film film, User user) {
        String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";

        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
    }

    @Override
    public Film removeLike(Film film, User user) {
        String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
        return film;
    }
}
