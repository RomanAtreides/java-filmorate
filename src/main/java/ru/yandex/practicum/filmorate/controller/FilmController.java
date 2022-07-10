package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    @Getter
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 0;

    private void generateFilmId(Film film) {
        film.setId(++filmId);
    }

    // Получение всех фильмов
    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    // Добавление фильма
    @PostMapping
    public Film create(@RequestBody Film film) {
        if (validate(film)) {
            generateFilmId(film);
            films.put(film.getId(), film);
            log.info("Новый фильм - \"{}\" добавлен в библиотеку", film.getName());
        }
        return film;
    }

    // Обновление фильма
    @PutMapping
    public Film put(@RequestBody Film film) {
        if (validate(film)) {
            films.put(film.getId(), film);
            log.info("Фильм - \"{}\" обновлён", film.getName());
        }
        return film;
    }

    public boolean validate(Film film) {
        boolean isValid = true;

        if (film.getId() < 0) {
            log.warn("Попытка добавить фильм с отрицательным id {}", film.getId());
            throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "id не может быть отрицательным!");
        }

        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Попытка добавить фильм с пустым именем");
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Название фильма не может быть пустым!");
        }

        if (film.getDescription().length() > 200) {
            log.warn("Попытка добавить фильм с длиной описания более 200 символов - {}",
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
            log.warn("Попытка добавить фильм с нулевой или отрицательной продолжительностью - {}",
                    film.getDuration());
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Продолжительность фильма должна быть положительной!");
        }
        return isValid;
    }
}
