package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    Genre getGenre(int genreId);

    List<Genre> getAllGenres();

    List<Genre> addGenreToFilm(List<Genre> genreIds, int filmId);

    List<Genre> getFilmsGenres(int filmId);

    List<Genre> update(Film newFilm);
}
