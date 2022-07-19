package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
}
