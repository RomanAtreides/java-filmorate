package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Film(
                resultSet.getLong("film_id"),
                resultSet.getString("film_name"),
                resultSet.getString("description"),
                resultSet.getDate("release_date").toLocalDate(),
                resultSet.getLong("duration"),
                new Genre(
                        resultSet.getInt("genre_id"), //todo: Столбец "genre_id" не найден
                        resultSet.getString("genre_name")
                ),
                new Mpa(
                        resultSet.getInt("rating_id"),
                        resultSet.getString("rating_name")
                )
        );
    }
}
