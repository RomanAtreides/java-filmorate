package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Film findFilmById(Long filmId);

    Collection<Film> findAll();

    Film create(Film film);

    Film put(Film film);

    List<Film> findPopularFilms(Long count);
}
