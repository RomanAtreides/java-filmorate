package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FilmController {
    /*
     * Убедитесь, что созданные контроллеры соответствуют правилам REST.
     * Добавьте в классы-контроллеры эндпоинты с подходящим типом запроса для каждого из случаев:
     * добавление фильма;
     * обновление фильма;
     * получение всех фильмов.
     * Эндпоинты для создания и обновления данных должны также вернуть созданную или изменённую сущность.
     */

    /*
     * Подсказка: про аннотацию @RequestBody.
     * Используйте аннотацию @RequestBody, чтобы создать объект из тела запроса на добавление или обновление сущности.
     */

    private final Map<String, Film> films = new HashMap<>();

    // Получение всех фильмов
    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    /*
     * Проверьте данные, которые приходят в запросе на добавление нового фильма или пользователя.
     * Эти данные должны соответствовать определённым критериям:
     * название не может быть пустым;
     * максимальная длина описания — 200 символов;
     * дата релиза — не раньше 28 декабря 1895 года;
     * продолжительность фильма должна быть положительной.
     */

    // Добавление фильма
    @PostMapping
    public Film create(@RequestBody Film film) {
        films.put(film.getName(), film);
        return film;
    }

    // Обновление фильма
    @PutMapping
    public Film put(@RequestBody Film film) {
        films.put(film.getName(), film);
        return film;
    }

    private boolean validate(Film film) {
        return false;
    }
}
