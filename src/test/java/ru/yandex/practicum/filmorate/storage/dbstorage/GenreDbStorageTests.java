package ru.yandex.practicum.filmorate.storage.dbstorage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTests {
    private final GenreStorage genreStorage;

    @Test
    public void shouldFindGenreById() {
        final int comedyGenreId = 1;
        final int dramaGenreId = 2;
        final int cartoonGenreId = 3;
        final int thrillerGenreId = 4;
        final int documentaryGenreId = 5;
        final int actionGenreId = 6;

        Genre genre01 = genreStorage.findGenreById(comedyGenreId);
        Genre genre02 = genreStorage.findGenreById(dramaGenreId);
        Genre genre03 = genreStorage.findGenreById(cartoonGenreId);
        Genre genre04 = genreStorage.findGenreById(thrillerGenreId);
        Genre genre05 = genreStorage.findGenreById(documentaryGenreId);
        Genre genre06 = genreStorage.findGenreById(actionGenreId);

        assertThat(genre01.getName()).isEqualTo("Комедия");
        assertThat(genre02.getName()).isEqualTo("Драма");
        assertThat(genre03.getName()).isEqualTo("Мультфильм");
        assertThat(genre04.getName()).isEqualTo("Триллер");
        assertThat(genre05.getName()).isEqualTo("Документальный");
        assertThat(genre06.getName()).isEqualTo("Боевик");
    }

    @Test
    public void shouldFindAllGenres() {
        final int genresCount = 6;

        assertThat(genreStorage.findAll().size()).isEqualTo(genresCount);
    }
}
