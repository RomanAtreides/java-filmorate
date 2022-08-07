package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

//@Data
@Getter
@Setter
//@AllArgsConstructor
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
    private Set<Long> friends = new HashSet<>();
    // private final boolean friendship; // unconfirmed = false, confirmed = true

    /*
     * 1. Добавьте статус для связи «дружба» между двумя пользователями:
     * 2. неподтверждённая — когда один пользователь отправил запрос на добавление другого пользователя в друзья,
     * 3. подтверждённая — когда второй пользователь согласился на добавление.
     */

    /*
     * Создание схемы базы данных
     * Начните с таблиц для хранения пользователей и фильмов. При проектировании помните о том, что:
     * - Каждый столбец таблицы должен содержать только одно значение.
     *   Хранить массивы значений или вложенные записи в столбцах нельзя.
     * - Все неключевые атрибуты должны однозначно определяться ключом.
     * - Все неключевые атрибуты должны зависеть только от первичного ключа, а не от других неключевых атрибутов.
     * - База данных должна поддерживать бизнес-логику, предусмотренную в приложении.
     *   Подумайте о том, как будет происходить получение всех фильмов, пользователей.
     *   А как — топ N наиболее популярных фильмов. Или список общих друзей с другим пользователем.
     */

    /*
     * Последние штрихи
     * Прежде чем отправлять получившуюся схему на проверку:
     * 1. Скачайте диаграмму в виде картинки и добавьте в репозиторий.
     *    Убедитесь, что на изображении чётко виден текст.
     * 2. Добавьте в файл README.md ссылку на файл диаграммы.
     *    Если использовать разметку markdown, то схему будет видно непосредственно в README.md.
     * 3. Там же напишите небольшое пояснение к схеме:
     *    приложите примеры запросов для основных операций вашего приложения.
     *
     * Документы по разметке, которая поддерживается GitHub, лежат здесь.
     * https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax#images
     */

    /*
     *
     */
}
