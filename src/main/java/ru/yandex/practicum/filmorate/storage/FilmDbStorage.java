package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
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

    /*@Override
    public Film findFilmById(Long filmId) {
        return films.get(filmId);
    }*/
    @Override
    public Film findFilmById(Long filmId) {
        //String sqlQuery = "SELECT film_id, film_name, description, release_date, duration FROM films WHERE film_id = ?";
        String sqlQuery = "SELECT film_id, film_name, description, release_date, duration, " +
                "genres.genre_id, genres.genre_name, ratings.rating_id, ratings.rating_name " +
                "FROM films " +
                "LEFT JOIN genres ON films.genre = genres.genre_id " +
                "LEFT JOIN ratings ON films.rating = ratings.rating_id " +
                "WHERE film_id = ?";

        Film film = jdbcTemplate.query(sqlQuery, new FilmMapper(), filmId).stream()
                .findAny()
                .orElse(null);

        return film;
        //return films.get(filmId);
    }

    /*@Override
    public Collection<Film> findAll() {
        return films.values();
    }*/
    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "SELECT film_id, film_name, description, release_date, duration, genre, rating " +
                "FROM films";
        Collection<Film> allFilms = jdbcTemplate.query(sqlQuery, new FilmMapper());

        return allFilms;
    }

    /*@Override
    public Film create(Film film) {
        films.put(film.getId(), film);
        return film;
    }*/
    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO films (film_name, description, release_date, duration, genre, rating) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
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

            final Genre genre = film.getGenre();

            if (genre == null) {
                //statement.setInt(5, 0);
                statement.setNull(5, Types.INTEGER);
                //throw new RuntimeException("Genre is null!");
            } else {
                statement.setInt(5, genre.getId());
            }
            //statement.setObject(5, film.getGenre());

            final Mpa mpa = film.getMpa();

            if (mpa == null) {
                //statement.setInt(6, 0);
                statement.setNull(6, Types.INTEGER);
                throw new RuntimeException("Mpa is null!");
            } else {
                statement.setInt(6, mpa.getId());
            }
            //statement.setObject(6, film.getMpa());
            return statement;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        films.put(film.getId(), film); // The line from the old implementation, should be deleted
        return film;
    }

    /*@Override
    public void put(Film film) {
        films.put(film.getId(), film);
    }*/
    @Override
    public void put(Film film) {
        String sqlQuery = "UPDATE films " +
                "SET film_name = ?, description = ?, release_date = ?, duration = ?, genre = ?, rating = ? " +
                "WHERE film_id = ?";

        /*jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"film_id"});



            return statement;
        });*/

        Integer genre = null;

        if (film.getGenre() != null) {
            genre = film.getGenre().getId();
        }

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
                genre,
                mpa,
                film.getId()
        );

        films.put(film.getId(), film); // The line from the old implementation, should be deleted
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
