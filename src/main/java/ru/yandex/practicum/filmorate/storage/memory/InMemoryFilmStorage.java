package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static final LocalDate FILM_MINIMUM_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static final int FILM_DESCRIPTIONS_LENGTH = 200;
    private final HashMap<Integer, Film> storage = new HashMap<>();
    private int id = 1;

    @Override
    public Film addFilm(Film film) {
        Film filmWithId = validateFilm(film).toBuilder().id(id++).build();
        if (filmWithId.getLikes() == null) {
            filmWithId.setLikes(new HashSet<>());
        }
        storage.put(filmWithId.getId(), filmWithId);
        return filmWithId;
    }

    @Override
    public Film updateFilm(Film film) {
        Film newFilm = validateFilm(film);
        if (newFilm.getLikes() == null) {
            newFilm.setLikes(new HashSet<>());
        }
        if (storage.containsKey(newFilm.getId())) {
            storage.put(newFilm.getId(), newFilm);
            return newFilm;
        } else {
            throw new NotFoundException("There is no such film in the library");
        }
    }

    @Override
    public Film deleteFilm(int filmId) {
        Film film;
        if (storage.containsKey(filmId)) {
            film = storage.get(filmId);
            storage.remove(filmId);
        } else {
            throw new NotFoundException("There is no such film in the library");
        }
        return film;
    }

    @Override
    public Film getFilm(int filmId) {
        if (storage.containsKey(filmId)) {
            return storage.get(filmId);
        } else throw new NotFoundException("There is no movie with such an id");
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean containsFilm(int filmId) {
        return storage.containsKey(filmId);
    }

    @Override
    public int getFilmCount() {
        return storage.size();
    }

    protected Film validateFilm(Film film) {
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
