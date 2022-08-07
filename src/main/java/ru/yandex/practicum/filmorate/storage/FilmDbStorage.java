package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/*
 * 1. Вам пригодятся созданные ранее интерфейсы UserStorage и FilmStorage.
 * Напишите для них новую имплементацию — например, UserDbStorage и FilmDbStorage.
 * Эти классы будут DAO — объектами доступа к данным.
 *
 * 2. Напишите в DAO соответствующие мапперы и методы,
 * позволяющие сохранять пользователей и фильмы в базу данных и получать их из неё.
 */

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Getter
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film findFilmById(Long filmId) {
        String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";

        /*
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, filmId);

        if (filmRows.next()) {
            Film film = new Film(
                    filmRows.getString("film_name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date"),
                    filmRows.getLong("duration")
                    //filmRows.getString("genre"),
                    //filmRows.getString("rating")
            );
        }
        jdbcTemplate.query(sqlQuery, (rs, (rs, rowNum) -> makeP));
        return films.get(filmId);
        */

        final List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, filmId);

        if (films.size() != 1) {
            return null;
        }
        return films.get(0);
        //return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        /*return Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("film_name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("releaseDate").toLocalDate())
                .duration(resultSet.getLong("duration"))
                .likes(new HashSet<>())
                .genre(resultSet.getString("genre"))
                .rating(resultSet.getString("rating"))
                .build();*/
        return null;
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
