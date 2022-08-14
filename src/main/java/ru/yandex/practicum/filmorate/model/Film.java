package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
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
    private final List<Genre> genres;
    private final Mpa mpa;
}
