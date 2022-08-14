package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final UserService userService;
    private long filmId = 0;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, UserService userService, LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.likeStorage = likeStorage;
    }

    private long generateFilmId() {
        return ++filmId;
    }

    public Film findFilmById(Long filmId) {
        Film film = filmStorage.findFilmById(filmId);

        validate(film);
        return film;
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        validate(film);
        film.setId(generateFilmId());
        return filmStorage.create(film);
    }

    public void put(Film film) {
        validate(film);
        filmStorage.put(film);
    }

    public List<Film> findPopularFilms(Long count) {
        if (count == null || count == 0) {
            count = 10L;
        }
        return filmStorage.findPopularFilms(count);
    }

    public Film addLike(Long filmId, Long userId) {
        User user = userService.findUserById(userId);
        Film film = findFilmById(filmId);

        likeStorage.addLike(film, user);
        return film;
    }

    public Film removeLike( Long filmId, Long userId) {
        User user = userService.findUserById(userId); //todo: test user to be not null
        Film film = findFilmById(filmId);

        return likeStorage.removeLike(film, user);
    }

    public void validate(Film film) {
        if (film == null) {
            log.warn("Попытка получить фильм по несуществующему id");
            throw new FilmNotFoundException("Фильм с таким id не найден!");
        }

        if (film.getId() < 0) {
            log.warn("Попытка добавить фильм с отрицательным id ({})", film.getId());
            throw new FilmNotFoundException("id не может быть отрицательным!");
        }

        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Попытка добавить фильм с пустым названием");
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Название фильма не может быть пустым!");
        }

        if (film.getDescription().length() > 200) {
            log.warn("Попытка добавить фильм с длиной описания более 200 символов ({})",
                    film.getDescription().length());
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Длина описания фильма должна быть не более 200 символов!");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Попытка добавить фильм с датой релиза {}, но она не может быть раньше 28.12.1895",
                    film.getReleaseDate());
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Дата релиза фильма должна быть не раньше 28.12.1895 года!");
        }

        if (film.getDuration() <= 0) {
            log.warn("Попытка добавить фильм с нулевой или отрицательной продолжительностью ({})",
                    film.getDuration());
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Продолжительность фильма должна быть положительной!");
        }
    }
}
