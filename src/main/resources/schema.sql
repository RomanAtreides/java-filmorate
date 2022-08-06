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

-- Создание таблицы genres
CREATE TABLE IF NOT EXISTS genres (
    genre_id INT PRIMARY KEY,
    genre_name VARCHAR(30) NOT NULL
    -- CONSTRAINT genre_pkey PRIMARY KEY (genre_id)
);

-- Создание таблицы ratings
CREATE TABLE IF NOT EXISTS ratings (
    rating_id INT PRIMARY KEY,
    rating_name VARCHAR(30) NOT NULL
);

-- Создание таблицы films
CREATE TABLE IF NOT EXISTS films (
    film_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    film_name VARCHAR(100) NOT NULL,
    description VARCHAR(200) NOT NULL,
    release_date DATE,
    duration REAL,
    likes BIGINT NOT NULL,
    genre VARCHAR(30) NOT NULL,
    rating VARCHAR(30) NOT NULL
);

-- Создание таблицы users
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL,
    birthday DATE,
    user_name VARCHAR(255) NOT NULL,
    friends BIGINT NOT NULL,
    friendship BOOLEAN
);