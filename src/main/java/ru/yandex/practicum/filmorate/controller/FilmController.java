package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
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
        if (films.containsKey(film.getName())) {
            throw new ValidationException("Фильм с таким названием уже есть в библиотеке!");
        }

        if (validate(film)) {
            films.put(film.getName(), film);
        }
        return film;
    }

    // Обновление фильма
    @PutMapping
    public Film put(@RequestBody Film film) {
        if (validate(film)) {
            films.put(film.getName(), film);
        }
        return film;
    }

    private boolean validate(Film film) {
        boolean isValid = true;

        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым!");
        }

        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания фильма — 200 символов!");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма должна быть не раньше 28 декабря 1895 года!");
        }

        if (film.getDuration() <= 0) {
            throw new RuntimeException("Продолжительность фильма должна быть положительной!");
        }
        return isValid;
    }
}
