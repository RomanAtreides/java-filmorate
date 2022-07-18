package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

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
    private int filmId = 0;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    private void generateFilmId(Film film) {
        film.setId(++filmId);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public void create(Film film) {
        generateFilmId(film);
        filmStorage.create(film);
        log.info("Новый фильм - \"{}\" добавлен в библиотеку", film.getName());
    }

    public void put(Film film) {
        filmStorage.put(film);
        log.info("Фильм - \"{}\" обновлён", film.getName());
    }
}
