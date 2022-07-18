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
