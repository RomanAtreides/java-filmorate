package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    @Getter
    private final Map<Long, Film> films = new HashMap<>();
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Film createFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return new Film(
                resultSet.getLong("film_id"),
                resultSet.getString("film_name"),
                resultSet.getString("description"),
                resultSet.getDate("release_date").toLocalDate(),
                resultSet.getLong("duration"),
                new Rating(resultSet.getInt("ratings.rating_id"), resultSet.getString("ratigns.rating_name"))
        );
    }

    @Override
    public Film findFilmById(Long filmId) {
        return films.get(filmId);
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void put(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public List<Film> findPopularFilms(Long count) {
        List<Film> popularFilms = new ArrayList<>(films.values());

        return popularFilms.stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film addLike(Film film, User user) {
        film.getLikes().add(user);
        return film;
    }

    @Override
    public Film removeLike(Film film, User user) {
        film.getLikes().remove(user);
        return film;
    }
}
