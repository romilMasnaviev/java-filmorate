DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS film_genres;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS mpa;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS friends;
DROP TABLE IF EXISTS users;


CREATE TABLE IF NOT EXISTS mpa
(
    mpa_id   INT PRIMARY KEY AUTO_INCREMENT,
    mpa_name VARCHAR(255)
);

ALTER TABLE mpa ALTER COLUMN mpa_id RESTART WITH 1;

INSERT INTO mpa (MPA_NAME)
VALUES
    ('G'),
    ('PG'),
    ('PG-13'),
    ('R'),
    ('NC-17');

CREATE TABLE IF NOT EXISTS genres
(
    genre_id   INT PRIMARY KEY AUTO_INCREMENT,
    genre_name VARCHAR(255)
);

INSERT INTO genres (genre_name)
VALUES
    ('Комедия'),
    ('Драма'),
    ('Мультфильм'),
    ('Триллер'),
    ('Документальный'),
    ('Боевик');

CREATE TABLE IF NOT EXISTS films
(
    film_id           INT PRIMARY KEY AUTO_INCREMENT,
    film_name         VARCHAR(255) NOT NULL,
    film_release_date DATE,
    film_description  VARCHAR(255),
    film_duration     INT,
    film_rate         DECIMAL(3, 2),
    film_mpa_id       INT,
    FOREIGN KEY (film_mpa_id) REFERENCES mpa (mpa_id)
);

ALTER TABLE films ALTER COLUMN film_id RESTART WITH 1; //TODO

CREATE TABLE IF NOT EXISTS film_genres
(
    film_id  INT,
    genre_id INT,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films (film_id),
    FOREIGN KEY (genre_id) REFERENCES genres (genre_id)
);

ALTER TABLE films ALTER COLUMN film_id RESTART WITH 1; //TODO


CREATE TABLE IF NOT EXISTS users
(
    user_id       INT PRIMARY KEY AUTO_INCREMENT,
    user_email    VARCHAR(255) NOT NULL,
    user_login    VARCHAR(255) NOT NULL,
    user_name     VARCHAR(255),
    user_birthday DATE
);
ALTER TABLE films ALTER COLUMN film_id RESTART WITH 1; //TODO

ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1;

CREATE TABLE IF NOT EXISTS friends
(
    user_id   INT,
    friend_id INT,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (friend_id) REFERENCES users (user_id)
);
ALTER TABLE films ALTER COLUMN film_id RESTART WITH 1; //TODO


CREATE TABLE IF NOT EXISTS likes
(
    user_id INT,
    film_id INT,
    PRIMARY KEY (user_id, film_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (film_id) REFERENCES films (film_id)
);
ALTER TABLE films ALTER COLUMN film_id RESTART WITH 1; //TODO


