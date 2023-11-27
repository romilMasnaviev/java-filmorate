package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface LikeStorage {

    void like(int userId, int filmId);

    void unlike(int userId, int filmId);

    List<Film> getPopular(int count);

    Map<Integer, List<Integer>> getTopFilmsGenres(int filmCount);

    Map<Integer, List<Integer>> getAllFilmsGenres();
}
