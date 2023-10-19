package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController()
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage storage;
    private final FilmService service;

    @Autowired
    public FilmController(FilmStorage storage, FilmService service) {
        this.storage = storage;
        this.service = service;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Received request to add new film");
        return storage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Received request to update film");
        return storage.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Received request to get all films");
        return storage.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id){
        return storage.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("Received request to like film");
        service.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void unLikeFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("Received request to dislike film");
        service.unLikeFilm(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam Optional<String> count) {
        return service.getTopFilms(Integer.parseInt(count.orElse("10")));
    }

}
