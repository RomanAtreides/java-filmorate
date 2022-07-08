package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    @Getter
    private final Map<String, Film> films = new HashMap<>();
    private int filmId = 0;

    private void generateFilmId(Film film) {
        film.setId(++filmId);
    }

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
            log.warn("Попытка добавить фильм с уже существующим в библиотеке названием - \"{}\"", film.getName());
            throw new ValidationException("Фильм с таким названием уже есть в библиотеке!");
        }

        if (validate(film)) {
            generateFilmId(film);
            log.info("Новый фильм - \"{}\" успешно добавлен в библиотеку", film.getName());
            films.put(film.getName(), film);
        }
        return film;
    }

    // Обновление фильма
    @PutMapping
    public Film put(@RequestBody Film film) {
        if (validate(film)) {
            log.info("Фильм - \"{}\" обновлён", film.getName());
            films.put(film.getName(), film);
        }
        return film;
    }

    public boolean validate(Film film) {
        boolean isValid = true;

        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Попытка добавить фильм с пустым именем");
            throw new ValidationException("Название фильма не может быть пустым!");
        }

        if (film.getDescription().length() > 200) {
            log.warn("Попытка добавить фильм с описанием, длина которого {} символа," +
                            " но оно не может быть больше 200 символов",
                    film.getDescription().length());
            throw new ValidationException("Максимальная длина описания фильма — 200 символов!");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Попытка добавить фильм с датой релиза {}, но она не может быть раньше 28.12.1895",
                    film.getReleaseDate());
            throw new ValidationException("Дата релиза фильма должна быть не раньше 28 декабря 1895 года!");
        }

        if (film.getDuration().isZero() || film.getDuration().isNegative()) {
            log.warn("Попытка добавить фильм с нулевой или отрицательной продолжительностью - {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }
        return isValid;
    }
}
