package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.db.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final LikeStorage likeStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, MpaStorage mpaStorage, GenreDbStorage genreStorage, LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
        this.likeStorage = likeStorage;
    }

    public void likeFilm(int id, int userId) {
        if (filmStorage.containsFilm(id)) {
            if (userId > 0) {
                likeStorage.like(userId, id);
            } else throw new NotFoundException("There is no user with this id");
        } else throw new NotFoundException("There is no movie with this id");
    }

    public void unLikeFilm(int id, int userId) {
        if (filmStorage.containsFilm(id)) {
            if (userId > 0) {
                likeStorage.unlike(userId, id);
            } else throw new NotFoundException("There is no user with this id");
        } else throw new NotFoundException("There is no movie with this id");
    }

    public List<Film> getTopFilms(int size) {
        List<Film> films = likeStorage.getPopular(size);
        List<Mpa> mpa = mpaStorage.getAllMpa();
        for (Film film : films) {
            film.setMpa(mpa.get(film.getMpa().getId() - 1));
            film.setGenres(genreStorage.getFilmsGenres(film.getId()));
        }
        return films;
    }

    public Film addFilm(Film film) {
        Film film1 = filmStorage.addFilm(film);
        if (film1.getGenres() != null) {
            film1.setGenres(genreStorage.addGenreToFilm(film.getGenres(), film.getId()));
        }
        return film1;
    }

    public Film updateFilm(Film film) {
        if (!filmStorage.containsFilm(film.getId())) {
            throw new NotFoundException("There's no such movie in the storage ");
        }
        Film newFilm = filmStorage.updateFilm(film);
        newFilm.setMpa(mpaStorage.getMpa(film.getMpa().getId()));
        if (film.getGenres() != null) {
            newFilm.setGenres(genreStorage.update(newFilm));
        }
        return newFilm;
    }

    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getAllFilms();
        List<Mpa> mpa = mpaStorage.getAllMpa();
        for (Film film : films) {
            film.setMpa(mpa.get(film.getMpa().getId() - 1));
            film.setGenres(genreStorage.getFilmsGenres(film.getId()));
        }
        return films;
    }

    public Film getFilm(int id) {
        Film film = filmStorage.getFilm(id);
        film.setMpa(mpaStorage.getMpa(film.getMpa().getId()));
        film.setGenres(genreStorage.getFilmsGenres(film.getId()));
        return film;
    }

}
