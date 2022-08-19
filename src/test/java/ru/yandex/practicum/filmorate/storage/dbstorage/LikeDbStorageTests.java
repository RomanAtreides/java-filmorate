package ru.yandex.practicum.filmorate.storage.dbstorage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeDbStorageTests {
    private final LikeStorage likeStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Test
    public void shouldAddFilmLikeAndRemoveFilmLike() {
        Film newFilm02 = new Film(
                2L,
                "new_film_02",
                "very_interesting_film",
                LocalDate.of(1999, 9, 9),
                3636,
                new ArrayList<>(),
                new Mpa(1, "G")
        );

        User newUser02 = new User(
                2,
                "new@email",
                "new_login",
                LocalDate.of(1992, 2, 2),
                "new_name",
                new Friendship(0, 0)
        );

        filmStorage.create(newFilm02);
        userStorage.create(newUser02);

        int filmLikesCountBeforeAdd =  filmStorage.findFilmById(newFilm02.getId()).getLikes().size();

        likeStorage.addLike(newFilm02, newUser02);

        Film filmWithLikes = filmStorage.findFilmById(newFilm02.getId());
        int filmLikesCountAfterAdd = filmStorage.findFilmById(newFilm02.getId()).getLikes().size();

        assertThat(filmWithLikes.getLikes()).contains(newUser02);
        assertThat(filmLikesCountAfterAdd).isEqualTo(filmLikesCountBeforeAdd + 1);
        likeStorage.removeLike(newFilm02, newUser02);

        Film filmWithoutLikes = filmStorage.findFilmById(newFilm02.getId());
        int filmLikesCountAfterRemove = filmStorage.findFilmById(newFilm02.getId()).getLikes().size();

        assertThat(filmWithoutLikes.getLikes()).doesNotContain(newUser02);
        assertThat(filmLikesCountAfterRemove).isEqualTo(filmLikesCountAfterAdd - 1);
    }
}
