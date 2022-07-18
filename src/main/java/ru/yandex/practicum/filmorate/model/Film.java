package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    /*
     * Подсказка: про список друзей и лайки.
     * Есть много способов хранить информацию о том, что два пользователя являются друзьями.
     * Например, можно создать свойство friends в классе пользователя, которое будет содержать список его друзей.
     * Вы можете использовать такое решение или придумать своё.
     * Для того чтобы обеспечить уникальность значения (мы не можем добавить одного человека в друзья дважды),
     * проще всего использовать для хранения Set<Long> c id друзей.
     * Таким же образом можно обеспечить условие «один пользователь — один лайк» для оценки фильмов.
     */

    private int id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final long duration;
}
