package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

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
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film findFilmById(Long filmId) {
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

        return popularFilms.stream()
                .sorted(Comparator.comparingLong((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film addLike(Long filmId, User user) {
        Film film = films.get(filmId);

        film.getLikes().add(user);
        return film;
    }

    @Override
    public Film removeLike(Long filmId, User user) {
        Film film = films.get(filmId);

        film.getLikes().remove(user);
        return film;
    }
}
