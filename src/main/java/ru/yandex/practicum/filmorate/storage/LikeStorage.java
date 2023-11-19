package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeStorage {

    void like(int userId, int filmId);

    void unlike(int userId, int filmId);

    List<Film> getPopular(int count);
}
