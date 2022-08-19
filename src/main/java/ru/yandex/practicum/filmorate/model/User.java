package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class User {
    @NonNull
    private long id;
    private final String email;
    private final String login;
    private final LocalDate birthday;
    @NonNull
    private String name;
    private final Set<Long> friends = new HashSet<>();
    private final Friendship friendship;
}
