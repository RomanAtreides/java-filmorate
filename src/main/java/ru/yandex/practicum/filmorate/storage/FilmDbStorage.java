package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
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

    private Film mapToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return new Film(
                resultSet.getLong("film_id"),
                resultSet.getString("film_name"),
                resultSet.getString("description"),
                resultSet.getDate("release_date").toLocalDate(),
                resultSet.getLong("duration")//todo,
                /*new Genre(
                        resultSet.getInt("genres.genre_id"),
                        resultSet.getString("genres.genre_name")
                ),
                new Rating(
                        resultSet.getInt("ratings.rating_id"),
                        resultSet.getString("ratings.rating_name")
                )*/
        );
    }

    @Override
    public Film findFilmById(Long filmId) {
        return films.get(filmId);
    }
    /*@Override
    public Film findFilmById(Long filmId) {
        String sqlQuery = "SELECT film_id, film_name, description, release_date, duration FROM films WHERE film_id = ?";

        Film film = jdbcTemplate.query(sqlQuery, this::mapToFilm, filmId).stream()
                .findAny()
                .orElse(null);

        return film;
        //return films.get(filmId);
    }*/

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }
    /*@Override
    public Collection<Film> findAll() {
        String sqlQuery = "SELECT film_id, film_name, description, release_date, duration FROM films";
        Collection<Film> allFilms = jdbcTemplate.query(sqlQuery, this::mapToFilm);
        return allFilms;
    }*/

    /*@Override
    public Film create(Film film) {
        films.put(film.getId(), film);
        return film;
    }*/
    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO films (film_name, description, release_date, duration) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"film_id"});

            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());

            final LocalDate releaseDate = film.getReleaseDate();

            if (releaseDate == null) {
                statement.setNull(3, Types.DATE);
            } else {
                statement.setDate(3, Date.valueOf(releaseDate));
            }
            statement.setLong(4, film.getDuration());
            return statement;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        films.put(film.getId(), film); // The line from the old implementation, should be deleted
        return film;
    }

    @Override
    public void put(Film film) {
        films.put(film.getId(), film);
    }
    /*@Override
    public void put(Film film) {
        String sqlQuery = "UPDATE films SET film_name = ?, description = ?, release_date = ?, duration = ? WHERE film_id = ?";

        jdbcTemplate.update(
                sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId()
        );

        films.put(film.getId(), film); // The line from the old implementation, should be deleted
    }*/

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
