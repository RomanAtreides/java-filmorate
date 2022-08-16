DELETE FROM users;
INSERT INTO users (email, login, birthday, user_name)
VALUES ('user01@email', 'usr01', '1991-01-01', 'user01');

INSERT INTO users (email, login, birthday, user_name)
VALUES ('user02@email', 'usr02', '1992-02-02', 'user02');

INSERT INTO users (email, login, birthday, user_name)
VALUES ('user03@email', 'usr03', '1993-03-03', 'user03');

DELETE FROM films;
INSERT INTO films (film_name, description, release_date, duration, rating)
VALUES ('film_01_name', 'film_01_description', '2001-11-11', '3600', 1);

INSERT INTO films (film_name, description, release_date, duration, rating)
VALUES ('film_02_name', 'film_02_description', '2002-12-12', '3700', 2);

INSERT INTO films (film_name, description, release_date, duration, rating)
VALUES ('film_03_name', 'film_03_description', '2003-09-09', '3800', 3);
