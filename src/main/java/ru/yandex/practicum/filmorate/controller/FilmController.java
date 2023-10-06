package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmorateStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController()
@RequestMapping("/films")
public class FilmController extends FilmorateStorage {


    @PostMapping
    public Film addFilm(@RequestBody String gsonString) {
        log.info("Received request to add new film");
        Film filmWithId = validateFilm(gsonString).toBuilder().id(id++).build();
        filmorateStorageHashMap.put(filmWithId.getId(), filmWithId);
        log.info("Film added successfully: {}", filmWithId);
        return filmWithId;
    }

    @PutMapping
    public Film updateFilm(@RequestBody String gsonString) {
        log.info("Received request to update film");
        Film newFilm = validateFilm(gsonString);
        if (filmorateStorageHashMap.containsKey(newFilm.getId())) {
            filmorateStorageHashMap.put(newFilm.getId(), newFilm);
            log.info("Film updated successfully: {}", newFilm);
            return newFilm;
        } else {
            log.error("Failed to update film: film with ID {} doesnt exists", newFilm.getId());
            throw new NotFoundException("There is no such film in the library");
        }
    }

    @GetMapping
    public List<Object> getAllFilms() {
        log.info("Received request to get all films");
        return new ArrayList<>(filmorateStorageHashMap.values());
    }

    private Film validateFilm(String gsonString) {
        Film film = gson.fromJson(gsonString, Film.class);
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
