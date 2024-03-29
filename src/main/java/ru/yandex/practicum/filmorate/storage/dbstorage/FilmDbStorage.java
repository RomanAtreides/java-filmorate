package ru.yandex.practicum.filmorate.storage.dbstorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.MpaIsNullException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.*;

@Repository("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film findFilmById(Long filmId) {
        String sqlQueryToGetFilm = "SELECT film_id, film_name, description, release_date, duration, " +
                "ratings.rating_id, ratings.rating_name " +
                "FROM films " +
                "LEFT JOIN ratings ON films.rating = ratings.rating_id " +
                "WHERE film_id = ?";

        Film film = jdbcTemplate.query(sqlQueryToGetFilm, new FilmMapper(), filmId).stream()
                .findAny()
                .orElse(null);

        String sqlQueryToGetFilmGenres = "SELECT genre_id, genre_name " +
                "FROM genres " +
                "WHERE genre_id IN(SELECT genre_id FROM film_genres WHERE film_id = ?)";

        String sqlQueryToGetFilmLikes = "SELECT user_id, email, login, birthday, user_name FROM users WHERE user_id IN (SELECT user_id FROM likes WHERE film_id = ?)";

        if (film != null) {
            List<Genre> filmGenres = jdbcTemplate.query(sqlQueryToGetFilmGenres, new GenreMapper(), film.getId());

            film.getGenres().addAll(filmGenres);

            List<User> filmLikes = jdbcTemplate.query(sqlQueryToGetFilmLikes, new UserMapper(), film.getId());

            film.getLikes().addAll(filmLikes);
        }
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "SELECT film_id, film_name, description, release_date, duration, ratings.rating_id, ratings.rating_name FROM films JOIN ratings ON films.rating = ratings.rating_id";

        return jdbcTemplate.query(sqlQuery, new FilmMapper());
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO films (film_name, description, release_date, duration, rating) " +
                "VALUES (?, ?, ?, ?, ?)";
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

            final Mpa mpa = film.getMpa();

            if (mpa == null) {
                statement.setNull(5, Types.INTEGER);
                throw new MpaIsNullException("У фильма отсутствует рейтинг!");
            } else {
                statement.setInt(5, mpa.getId());
            }
            return statement;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        String sqlQueryToSaveFilmGenres = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";

        if (film.getGenres() != null) {
            for (int i = 0; i < film.getGenres().size(); i++) {
                jdbcTemplate.update(
                        sqlQueryToSaveFilmGenres,
                        film.getId(),
                        film.getGenres().get(i).getId()
                );
            }
        }
        return film;
    }

    @Override
    public Film put(Film film) {
        String sqlQueryToUpdateFilm = "UPDATE films " +
                "SET film_name = ?, description = ?, release_date = ?, duration = ?, rating = ? " +
                "WHERE film_id = ?";

        String sqlQueryToDeleteFilmGenres = "DELETE FROM film_genres WHERE film_id = ?";
        String sqlQueryToUpdateFilmGenres = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";

        updateFilmToPut(sqlQueryToUpdateFilm, film);
        jdbcTemplate.update(sqlQueryToDeleteFilmGenres, film.getId());
        updateFilmGenres(film, sqlQueryToUpdateFilmGenres);
        return findFilmById(film.getId());
    }

    private void updateFilmToPut(String sqlQuery, Film film) {
        Integer mpa = null;

        if (film.getMpa() != null) {
            mpa = film.getMpa().getId();
        }
        jdbcTemplate.update(
                sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                mpa,
                film.getId()
        );
    }

    private void updateFilmGenres(Film film, String sqlQuery) {
        if (film.getGenres() != null) {
            Set<Genre> filmGenres = new HashSet<>(film.getGenres());
            Genre[] genres = new Genre[filmGenres.size()];

            filmGenres.toArray(genres);

            for (Genre genre : genres) {
                jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
            }
        }
    }

    @Override
    public List<Film> findPopularFilms(Long count) {
        String sqlQuery = "SELECT f.film_id, f.film_name, f.description, f.release_date, f.duration, " +
                "r.rating_id, r.rating_name " +
                "FROM films AS f " +
                "JOIN ratings AS r ON f.rating = r.rating_id " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(l.user_id) DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, new FilmMapper(), count);
    }
}
