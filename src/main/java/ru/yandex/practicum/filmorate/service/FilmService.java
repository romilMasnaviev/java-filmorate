package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public void likeFilm(int id, int userId) {
        if (storage.containsFilm(id)) {
            if(userId >0){
                storage.getFilm(id).getLikes().add(userId);
            } else throw new NotFoundException("There is no user with this id");
        } else throw new NotFoundException("There is no movie with this id");
    }

    public void unLikeFilm(int id, int userId) {
        if (storage.containsFilm(id)){
            if(userId >0){
                storage.getFilm(id).getLikes().remove(userId);
            } else throw new NotFoundException("There is no user with this id");
        } else throw new NotFoundException("There is no movie with this id");
    }

    public List<Film> getTopFilms(int size) {
        List<Film> sortedFilms = storage.getAllFilms().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .collect(Collectors.toList());

        if (size < sortedFilms.size()) {
            sortedFilms = sortedFilms.subList(0, size);
        }

        return sortedFilms;
    }

}
