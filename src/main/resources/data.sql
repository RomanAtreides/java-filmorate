-- Заполнение данными таблицы genres
MERGE INTO PUBLIC.genres (genre_id, genre_name) VALUES ( 1, 'Комедия' );
MERGE INTO PUBLIC.genres (genre_id, genre_name) VALUES ( 2, 'Драма' );
MERGE INTO PUBLIC.genres (genre_id, genre_name) VALUES ( 3, 'Мультфильм' );
MERGE INTO PUBLIC.genres (genre_id, genre_name) VALUES ( 4, 'Триллер' );
MERGE INTO PUBLIC.genres (genre_id, genre_name) VALUES ( 5, 'Документальный' );
MERGE INTO PUBLIC.genres (genre_id, genre_name) VALUES ( 6, 'Боевик' );

-- Заполнение данными таблицы ratings
MERGE INTO PUBLIC.ratings (rating_id, rating_name) VALUES ( 1, 'G' );
MERGE INTO PUBLIC.ratings (rating_id, rating_name) VALUES ( 2, 'PG' );
MERGE INTO PUBLIC.ratings (rating_id, rating_name) VALUES ( 3, 'PG-13' );
MERGE INTO PUBLIC.ratings (rating_id, rating_name) VALUES ( 4, 'R' );
MERGE INTO PUBLIC.ratings (rating_id, rating_name) VALUES ( 5, 'NC-17' );
