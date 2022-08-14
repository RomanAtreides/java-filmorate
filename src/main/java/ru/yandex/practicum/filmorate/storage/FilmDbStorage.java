package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.*;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    @Getter
    private final JdbcTemplate jdbcTemplate;

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

        if (film != null) {
            List<Genre> filmGenres = jdbcTemplate.query(sqlQueryToGetFilmGenres, new GenreMapper(), film.getId());
            film.getGenres().addAll(filmGenres);
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
                throw new RuntimeException("Mpa is null!");
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
        Integer mpa = null;

        String sqlQueryToUpdateFilm = "UPDATE films " +
                "SET film_name = ?, description = ?, release_date = ?, duration = ?, rating = ? " +
                "WHERE film_id = ?";

        String sqlQueryToDeleteFilmGenres = "DELETE FROM film_genres WHERE film_id = ?";
        String sqlQueryToUpdateFilmGenres = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";

        if (film.getMpa() != null) {
            mpa = film.getMpa().getId();
        }

        jdbcTemplate.update(
                sqlQueryToUpdateFilm,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                mpa,
                film.getId()
        );

        jdbcTemplate.update(sqlQueryToDeleteFilmGenres, film.getId());

        if (film.getGenres() != null) {
            Set<Genre> filmGenres = new HashSet<>(film.getGenres());
            Genre[] genres = new Genre[filmGenres.size()];

            filmGenres.toArray(genres);

            for (Genre genre : genres) {
                jdbcTemplate.update(sqlQueryToUpdateFilmGenres, film.getId(), genre.getId());
            }
        }
        return findFilmById(film.getId());
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
