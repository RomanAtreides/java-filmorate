package ru.yandex.practicum.filmorate.storage.dbstorage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTests {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;

    @Test
    public void shouldFindFilmById() {
        long filmId = 1L;
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.findFilmById(filmId));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("id", filmId));
    }

    @Test
    public void shouldCreateFilmAndFindAllFilms() {
        Film newFilm04 = new Film(
                4L,
                "new_film_04",
                "very_interesting_film",
                LocalDate.of(1999, 9, 9),
                3636,
                new ArrayList<>(),
                new Mpa(1, "G")
        );

        int filmsCountBefore = filmStorage.findAll().size();

        filmStorage.create(newFilm04);

        int filmsCountAfter = filmStorage.findAll().size();

        assertThat(filmsCountAfter).isEqualTo(filmsCountBefore + 1);
        assertThat(filmStorage.findAll()).contains(newFilm04);
    }

    @Test
    public void shouldUpdateFilm() {
        Film film02 = filmStorage.findFilmById(2L);

        Film newFilm02 = new Film(
                2L,
                "new_film_02",
                "not_very_interesting_film",
                LocalDate.of(1999, 9, 9),
                3636,
                new ArrayList<>(),
                new Mpa(1, "G")
        );

        String filmOldDescription = filmStorage.findFilmById(film02.getId()).getDescription();
        int filmsCountBefore = filmStorage.findAll().size();

        assertThat(filmOldDescription).isEqualTo("film_02_description");
        filmStorage.put(newFilm02);

        String filmNewDescription = filmStorage.findFilmById(newFilm02.getId()).getDescription();
        int filmsCountAfter = filmStorage.findAll().size();

        assertThat(filmNewDescription).isEqualTo("not_very_interesting_film");
        assertThat(filmsCountBefore).isEqualTo(filmsCountAfter);
    }

    @Test
    public void shouldFindPopularFilms() {
        Film film01 = filmStorage.findFilmById(1L);
        Film film02 = filmStorage.findFilmById(2L);
        Film film03 = filmStorage.findFilmById(3L);
        User user01 = userStorage.findUserById(1L);
        User user02 = userStorage.findUserById(2L);
        User user03 = userStorage.findUserById(3L);
        final int mostPopularFilmIndex = 0;
        long filmsCount = 10L;

        likeStorage.addLike(film02, user01);
        assertThat(
                filmStorage.findPopularFilms(filmsCount).get(mostPopularFilmIndex).getId()).isEqualTo(film02.getId()
        );

        likeStorage.addLike(film03, user01);
        likeStorage.addLike(film03, user02);
        assertThat(
                filmStorage.findPopularFilms(filmsCount).get(mostPopularFilmIndex).getId()).isEqualTo(film03.getId()
        );

        likeStorage.addLike(film01, user01);
        likeStorage.addLike(film01, user02);
        likeStorage.addLike(film01, user03);
        assertThat(
                filmStorage.findPopularFilms(filmsCount).get(0).getId()).isEqualTo(film01.getId()
        );

        likeStorage.removeLike(film01, user03);
        likeStorage.removeLike(film01, user02);
        assertThat(
                filmStorage.findPopularFilms(filmsCount).get(mostPopularFilmIndex).getId()).isEqualTo(film03.getId()
        );

        filmsCount = 2L;
        List<Film> twoMostPopularFilms = filmStorage.findPopularFilms(filmsCount);

        assertThat(twoMostPopularFilms.size()).isEqualTo(filmsCount);

        filmsCount = 1L;
        List<Film> oneMostPopularFilm = filmStorage.findPopularFilms(filmsCount);

        assertThat(oneMostPopularFilm.size()).isEqualTo(filmsCount);
    }
}
