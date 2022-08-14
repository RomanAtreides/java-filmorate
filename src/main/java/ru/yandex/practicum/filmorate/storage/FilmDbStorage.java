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
        String sqlQueryToGetFilm = "SELECT film_id, film_name, description, release_date, duration, " +
                "ratings.rating_id, ratings.rating_name " +
                "FROM films " +
                "LEFT JOIN ratings ON films.rating = ratings.rating_id " +
                "WHERE film_id = ?";

        Film film = jdbcTemplate.query(sqlQueryToGetFilm, new FilmMapper(), filmId).stream()
                .findAny()
                .orElse(null);

        String sqlQueryToGetFilmGenres = "SELECT * FROM genres WHERE genre_id IN(SELECT genre_id FROM film_genres WHERE film_id = ?)";

        if (film != null) {
            List<Genre> filmGenres = jdbcTemplate.query(sqlQueryToGetFilmGenres, new GenreMapper(), film.getId());
            film.getGenres().addAll(filmGenres);
        }
        return film;
        //return films.get(filmId);
    }

    /*@Override
    public Collection<Film> findAll() {
        return films.values();
    }*/
    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "SELECT film_id, film_name, description, release_date, duration, ratings.rating_id, ratings.rating_name FROM films JOIN ratings ON films.rating = ratings.rating_id";
        //String sqlQuery = "SELECT film_id, film_name, description, release_date, duration, rating FROM films";
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

            /*final List<Genre> genres = film.getGenres();

            if (genres == null) {
                //statement.setInt(5, 0);
                statement.setNull(5, Types.ARRAY);
                //throw new RuntimeException("Genre is null!");
            } else {
                statement.setArray(5, null);
            }*/
            //statement.setObject(5, film.getGenre());

            final Mpa mpa = film.getMpa();

            if (mpa == null) {
                //statement.setInt(5, 0);
                statement.setNull(5, Types.INTEGER);
                throw new RuntimeException("Mpa is null!");
            } else {
                statement.setInt(5, mpa.getId());
            }
            //statement.setObject(6, film.getMpa());
            return statement;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        String sqlQueryToSaveFilmGenres = "INSERT INTO film_genres(film_id, genre_id) VALUES(?, ?)";

        if (film.getGenres() != null) {
            for (int i = 0; i < film.getGenres().size(); i++) {
                jdbcTemplate.update(
                        sqlQueryToSaveFilmGenres,
                        film.getId(),
                        film.getGenres().get(i).getId()
                );
            }
        }
        films.put(film.getId(), film); //todo: The line from the old implementation, should be deleted
        return film;
    }

    /*@Override
    public void put(Film film) {
        films.put(film.getId(), film);
    }*/
    @Override
    public void put(Film film) {
        Integer mpa = null;

        String sqlQuery = "UPDATE films " +
                "SET film_name = ?, description = ?, release_date = ?, duration = ?, rating = ? " +
                "WHERE film_id = ?";

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

        films.put(film.getId(), film); //todo: The line from the old implementation, should be deleted
    }

    @Override
    public List<Film> findPopularFilms(Long count) {
        /*List<Film> popularFilms = new ArrayList<>(films.values());

        return popularFilms.stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());*/

        //String sqlQuery = "SELECT f.film_id, f.film_name, f.description, f.release_date, f.duration, r.rating_id, r.rating_name FROM films f JOIN ratings r ON f.rating = r.rating_id JOIN likes l ON f.film_id = l.film_id ORDER BY COUNT(l.film_id) DESC LIMIT ?";

        String sqlQuery = "SELECT f.film_id, f.film_name, f.description, f.release_date, f.duration, r.rating_id, r.rating_name FROM films AS f JOIN ratings AS r ON f.rating = r.rating_id LEFT JOIN likes AS l ON f.film_id = l.film_id GROUP BY f.film_id ORDER BY COUNT(l.user_id) DESC LIMIT ?";

        //String sqlQuery = "SELECT film_id, film_name, description, release_date, duration, rating FROM films";
        List<Film> popularFilms = jdbcTemplate.query(sqlQuery, new FilmMapper(), count);

        return popularFilms;
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
