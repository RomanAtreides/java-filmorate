package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    /*
     * Новая логика.
     * Создайте FilmService, который будет отвечать за операции с фильмами,
     * добавление и удаление лайка,
     * вывод 10 наиболее популярных фильмов по количеству лайков.
     * Пусть пока каждый пользователь может поставить лайк фильму только один раз.
     *
     * Добавьте к ним аннотацию @Service — тогда к ним можно будет получить доступ из контроллера.
     */

    /*
     * Подсказка: @Service vs @Component.
     * @Component — аннотация, которая определяет класс как управляемый Spring.
     * Такой класс будет добавлен в контекст приложения при сканировании.
     * @Service не отличается по поведению, но обозначает более узкий спектр классов — такие,
     * которые содержат в себе бизнес-логику и, как правило, не хранят состояние.
     */

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private long filmId = 0;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    private void generateFilmId(Film film) {
        film.setId(++filmId);
    }

    public Film findFilmById(Long filmId) {
        return filmStorage.findFilmById(filmId);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public void create(Film film) {
        generateFilmId(film);
        filmStorage.create(film);
    }

    public void put(Film film) {
        filmStorage.put(film);
    }

    public List<Film> findPopularFilms(Long count) {
        if (count == null || count == 0) {
            count = 10L;
        }
        return filmStorage.findPopularFilms(count);
    }

    public Film addLike(Long filmId, Long userId) {
        User user = userStorage.findUserById(userId);
        Film film = filmStorage.findFilmById(filmId);

        return filmStorage.addLike(film, user);
    }

    public Film removeLike( Long filmId, Long userId) {
        User user = userStorage.findUserById(userId);
        Film film = filmStorage.findFilmById(filmId);

        return filmStorage.removeLike(film, user);
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
