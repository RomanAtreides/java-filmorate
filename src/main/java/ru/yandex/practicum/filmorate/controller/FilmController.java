package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmValidationService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    @Getter
    private final Map<Integer, Film> films = new HashMap<>();
    private final FilmValidationService filmValidationService = new FilmValidationService();
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
        if (filmValidationService.validate(film)) {
            generateFilmId(film);
            films.put(film.getId(), film);
            log.info("Новый фильм - \"{}\" добавлен в библиотеку", film.getName());
        }
        return film;
    }

    // Обновление фильма
    @PutMapping
    public Film put(@RequestBody Film film) {
        if (filmValidationService.validate(film)) {
            films.put(film.getId(), film);
            log.info("Фильм - \"{}\" обновлён", film.getName());
        }
        return film;
    }
}
