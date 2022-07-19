package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FilmValidationService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {
    /*
     * Зависимости.
     * Переделайте код в контроллерах, сервисах и хранилищах под использование внедрения зависимостей.
     *
     * Используйте аннотации @Service, @Component, @Autowired. Внедряйте зависимости через конструкторы классов.
     *
     * Классы-сервисы должны иметь доступ к классам-хранилищам.
     * Убедитесь, что сервисы зависят от интерфейсов классов-хранилищ, а не их реализаций.
     * Таким образом в будущем будет проще добавлять и использовать новые реализации с другим типом хранения данных.
     *
     * Сервисы должны быть внедрены в соответствующие контроллеры.
     */

    private final FilmValidationService filmValidationService;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmValidationService filmValidationService, FilmService filmService) {
        this.filmValidationService = filmValidationService;
        this.filmService = filmService;
    }

    // Получение фильма по идентификатору
    @GetMapping("/{filmId}")
    public Film findFilmById(@PathVariable Integer filmId) {
        Film film = filmService.findFilmById(filmId);

        filmValidationService.validate(film);
        return film;
    }

    // Получение списка всех фильмов
    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    // Добавление нового фильма
    @PostMapping
    public Film create(@RequestBody Film film) {
        if (filmValidationService.validate(film)) {
            filmService.create(film);
        }
        return film;
    }

    // Обновление существующего в базе фильма
    @PutMapping
    public Film put(@RequestBody Film film) {
        if (filmValidationService.validate(film)) {
            filmService.put(film);
        }
        return film;
    }
}
