package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class InMemoryFilmStorage implements FilmStorage{

    private static final LocalDate FILM_MINIMUM_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static final int FILM_DESCRIPTIONS_LENGTH = 200;
    private final HashMap<Integer, Film> filmStorage = new HashMap<>();
    private int id = 1;

    public Film addFilm(Film film) {
        Film filmWithId = validateFilm(film).toBuilder().id(id++).build();
        filmStorage.put(filmWithId.getId(), filmWithId);
        return filmWithId;
    }

    public Film updateFilm(Film film) {
        Film newFilm = validateFilm(film);
        if (filmStorage.containsKey(newFilm.getId())) {
            filmStorage.put(newFilm.getId(), newFilm);
            return newFilm;
        } else {
            throw new NotFoundException("There is no such film in the library");
        }
    }

    @Override
    public Film deleteFilm(int filmId) {
        //TODO
        return null;
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(filmStorage.values());
    }

    private Film validateFilm(Film film) {
        isValidName(film);
        isValidDescription(film);
        isValidDuration(film);
        isValidReleaseDate(film);
        return film;
    }

    private void isValidName(Film film) {
        if (film.getName().isEmpty()) {
            throw new ValidationException("The title of the movie is empty");
        }
    }

    private void isValidDescription(Film film) {
        if (film.getDescription().toCharArray().length > FILM_DESCRIPTIONS_LENGTH) {
            throw new ValidationException("Description length is more than " + FILM_DESCRIPTIONS_LENGTH + " characters");
        }
    }

    private void isValidReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(FILM_MINIMUM_RELEASE_DATE)) {
            throw new ValidationException("The release date of the film is earlier then " + FILM_MINIMUM_RELEASE_DATE);
        }
    }

    private void isValidDuration(Film film) {
        if (film.getDuration() < 1) {
            throw new ValidationException("The duration of the movie is less than 0");
        }
    }
}
