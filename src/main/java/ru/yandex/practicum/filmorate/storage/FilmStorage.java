package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    /*
     * Архитектура.
     * Создайте интерфейсы FilmStorage и UserStorage,
     * в которых будут определены методы добавления, удаления и модификации объектов.
     */

    Film findFilmById(Integer filmId);

    Collection<Film> findAll();

    void create(Film film);

    void put(Film film);

    List<Film> findPopularFilms(Long count);
}
