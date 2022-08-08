package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(of = "ratingId")
public class Rating {
    private final int ratingId;
    private final String ratingName;
}
