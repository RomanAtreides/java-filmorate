package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    /*
     * Архитектура.
     * Создайте интерфейсы FilmStorage и UserStorage,
     * в которых будут определены методы добавления, удаления и модификации объектов.
     */

    Film findFilmById(Long filmId);

    Collection<Film> findAll();

    void create(Film film);

    void put(Film film);

    List<Film> findPopularFilms(Long count);

    Film addLike(Film film, User user);

    Film removeLike(Film film, User user);
}
