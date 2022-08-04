/*
 Соберите SQL-запросы, формирующие структуру вашей базы, в отдельный файл в src/main/resources
 с названием schema.sql — так схема будет создаваться заново при каждом запуске приложения.

 Подсказка: про файл schema.sql
 Включите в файл schema.sql создание таблиц.
 Если вам нужны некоторые данные в базе,
 их инициализация обычно описывается в файле data.sql — создайте его там же, где и schema.sql.
 Чтобы избежать ошибок, связанных с многократным применением скрипта к БД,
 добавьте условие IF NOT EXISTS при создании таблиц и индексов.
 */

CREATE TABLE IF NOT EXISTS genres (
    genre_id INT NOT NULL,
    genre_name VARCHAR(30) NOT NULL,
    CONSTRAINT genre_pkey PRIMARY KEY (genre_id)
);

CREATE TABLE IF NOT EXISTS ratings (
    rating_id INT NOT NULL,
    rating_name VARCHAR(30) NOT NULL,
    CONSTRAINT rating_pkey PRIMARY KEY (rating_id)
);
