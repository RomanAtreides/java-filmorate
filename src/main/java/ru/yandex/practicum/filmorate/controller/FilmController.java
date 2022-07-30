package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    // Получение фильма по идентификатору
    @GetMapping("/{filmId}")
    public Film findFilmById(@PathVariable Long filmId) {
        return filmService.findFilmById(filmId);
    }

    // Получение списка всех фильмов
    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    // Добавление нового фильма
    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmService.create(film);
    }

    // Обновление существующего в базе фильма
    @PutMapping
    public Film put(@RequestBody Film film) {
        filmService.put(film);
        return film;
    }

    // Получение указанного количества популярных фильмов
    @GetMapping("/popular")
    public List<Film> findPopularFilms(@RequestParam(required = false) Long count) {
        return filmService.findPopularFilms(count);
    }

    // Добавление лайка фильму
    @PutMapping("/{filmId}/like/{userId}")
    public Film addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        return filmService.addLike(filmId, userId);
    }

    // Удаление лайка
    @DeleteMapping("/{filmId}/like/{userId}")
    public Film removeLike(@PathVariable Long filmId, @PathVariable Long userId) {
        return filmService.removeLike(filmId, userId);
    }
}
