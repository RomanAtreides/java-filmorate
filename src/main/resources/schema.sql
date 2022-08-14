CREATE TABLE IF NOT EXISTS genres (
    genre_id INT PRIMARY KEY,
    genre_name VARCHAR(30) NOT NULL
    -- CONSTRAINT genre_pkey PRIMARY KEY (genre_id)
);

CREATE TABLE IF NOT EXISTS ratings (
    rating_id INT PRIMARY KEY,
    rating_name VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    film_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    film_name VARCHAR(255) NOT NULL,
    description VARCHAR(200),
    release_date DATE CHECK release_date > '1895-12-28',
    duration BIGINT CHECK duration > 0,
    rating INT
);

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) CHECK(NOT NULL AND email LIKE '@'),
    login VARCHAR(255) CHECK(NOT NULL AND login NOT LIKE ' '),
    birthday DATE CHECK birthday < now(),
    user_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS friendship (
    user_id BIGINT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    friend_id BIGINT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT composite_friendship_key PRIMARY KEY(user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS film_genres (
    film_id BIGINT NOT NULL REFERENCES films(film_id) ON DELETE CASCADE,
    genre_id BIGINT NOT NULL REFERENCES genres(genre_id) ON DELETE CASCADE--,
    --CONSTRAINT composite_film_genres_key PRIMARY KEY(film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS likes (
    film_id BIGINT NOT NULL REFERENCES films(film_id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT composite_likes_key PRIMARY KEY (film_id, user_id)
);