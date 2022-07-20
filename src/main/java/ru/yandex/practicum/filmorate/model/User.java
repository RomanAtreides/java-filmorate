package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Set;

@Data
public class User {
    /*
     * Подсказка: про список друзей и лайки.
     * Есть много способов хранить информацию о том, что два пользователя являются друзьями.
     * Например, можно создать свойство friends в классе пользователя, которое будет содержать список его друзей.
     * Вы можете использовать такое решение или придумать своё.
     * Для того чтобы обеспечить уникальность значения (мы не можем добавить одного человека в друзья дважды),
     * проще всего использовать для хранения Set<Long> c id друзей.
     * Таким же образом можно обеспечить условие «один пользователь — один лайк» для оценки фильмов.
     */

    private long id;
    private final String email;
    private final String login;
    private final LocalDate birthday;
    @NonNull
    private String name;
    private Set<Long> friends;
}
