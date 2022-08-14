package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
@Service
public class GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre findGenreById(Integer genreId) {
        Genre genre = genreStorage.findGenreById(genreId);

        validate(genre);
        return genre;
    }

    public List<Genre> findAll() {
        return genreStorage.findAll();
    }

    public void validate(Genre genre) {
        if (genre == null) {
            throw new GenreNotFoundException("Жанр с таким id не найден!");
        }

        if (genre.getId() < 0) {
            throw new GenreNotFoundException("id не может быть отрицательным!");
        }
    }
}