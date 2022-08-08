package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(of = "genreId")
public class Genre {
    private final int genreId;
    private final String genreName;
}
