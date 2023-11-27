package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film deleteFilm(int filmId);

    Film getFilm(int filmId);

    List<Film> getAllFilms();

    boolean containsFilm(int filmId);

    int getFilmCount();
}
