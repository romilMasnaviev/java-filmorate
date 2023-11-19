package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db.GenreDbStorage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {

    private static final List<Genre> genres = new ArrayList<>();
    private final JdbcTemplate jdbcTemplate;
    private GenreDbStorage genreDbStorage;

    @BeforeAll
    static void setup() {
        genres.add(Genre.builder().id(1).name("Комедия").build());
        genres.add(Genre.builder().id(2).name("Драма").build());
        genres.add(Genre.builder().id(3).name("Мультфильм").build());
        genres.add(Genre.builder().id(4).name("Триллер").build());
        genres.add(Genre.builder().id(5).name("Документальный").build());
        genres.add(Genre.builder().id(6).name("Боевик").build());
    }

    @BeforeEach
    void set() {
        genreDbStorage = new GenreDbStorage(jdbcTemplate);
    }

    @Test
    void getGenre() {
        assertEquals(Genre.builder().id(1).name("Комедия").build(), genreDbStorage.getGenre(1));
    }

    @Test
    void getAllGenres() {
        assertEquals(genres, genreDbStorage.getAllGenres());
    }

}
