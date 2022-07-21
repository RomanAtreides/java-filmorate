package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    /*
     * Создайте классы InMemoryFilmStorage и InMemoryUserStorage, имплементирующие новые интерфейсы,
     * и перенесите туда всю логику хранения, обновления и поиска объектов.
     *
     * Добавьте к InMemoryFilmStorage и InMemoryUserStorage аннотацию @Component,
     * чтобы впоследствии пользоваться внедрением зависимостей и передавать хранилища сервисам.
     */

    @Getter
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film findFilmById(Integer filmId) {
        return films.get(filmId);
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public void create(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public void put(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public List<Film> findPopularFilms(Long count) {
        List<Film> popularFilms = new ArrayList<>(films.values());
        //todo: отсортировать список по лайкам
        return popularFilms.stream().limit(count).collect(Collectors.toList());
    }
}
