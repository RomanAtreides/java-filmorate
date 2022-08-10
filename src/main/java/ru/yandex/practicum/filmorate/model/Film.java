package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

//@Data
@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Film {
    @NonNull
    private long id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final long duration;
    private Set<User> likes = new HashSet<>();
    private final Genre genre;
    private final Mpa mpa;

    /*
     * 1.
     * Добавьте новое свойство — «жанр».
     * У фильма может быть сразу несколько жанров, а у поля — несколько значений.
     * Например, таких:
     * Комедия.
     * Драма.
     * Мультфильм.
     * Триллер.
     * Документальный.
     * Боевик.
     * 2.
     * Ещё одно свойство — рейтинг Ассоциации кинокомпаний (англ. Motion Picture Association, сокращённо МРА).
     * Эта оценка определяет возрастное ограничение для фильма.
     * Значения могут быть следующими:
     * G — у фильма нет возрастных ограничений,
     * PG — детям рекомендуется смотреть фильм с родителями,
     * PG-13 — детям до 13 лет просмотр не желателен,
     * R — лицам до 17 лет просматривать фильм можно только в присутствии взрослого,
     * NC-17 — лицам до 18 лет просмотр запрещён.
     */
}
