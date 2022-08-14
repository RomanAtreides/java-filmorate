package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component("genreDbStorage")
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre findGenreById(Integer genreId) {
        String sqlQuery = "SELECT genre_id, genre_name FROM genres WHERE genre_id = ?";

        Genre genre = jdbcTemplate.query(sqlQuery, new GenreMapper(), genreId).stream()
                .findAny()
                .orElse(null);

        return genre;
    }

    @Override
    public List<Genre> findAll() {
        String sqlQuery = "SELECT genre_id, genre_name FROM genres";
        List<Genre> allGenres = jdbcTemplate.query(sqlQuery, new GenreMapper());

        return allGenres;
    }
}
