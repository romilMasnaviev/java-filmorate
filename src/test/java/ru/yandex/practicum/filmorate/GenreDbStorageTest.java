package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db.GenreDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {

    private static final List<Genre> genres = new ArrayList<>();
    private final JdbcTemplate jdbcTemplate;
    Film film1 = Film.builder().name("Inception").description("A thief who steals corporate secrets through the use of dream-sharing technology").releaseDate(LocalDate.of(2010, 7, 16)).duration(148).mpa(Mpa.builder().id(1).build()).build();
    private GenreDbStorage genreDbStorage;
    private FilmDbStorage filmDbStorage;

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
        filmDbStorage = new FilmDbStorage(jdbcTemplate);
    }

    @Test
    void getGenre() {
        assertEquals(Genre.builder().id(1).name("Комедия").build(), genreDbStorage.getGenre(1));
    }

    @Test
    void getAllGenres() {
        assertEquals(genres, genreDbStorage.getAllGenres());
    }

    @Test
    void updateTest() {
        Film film = film1;

        //добавляем жанры
        film.setGenres(genres);
        //добавляем фильм
        filmDbStorage.addFilm(film);

        //обновляем жанры
        film.setGenres(genres.subList(0, 2));

        //обновляем фильм
        genreDbStorage.update(film);

        Film newFilm = film1.toBuilder().genres(genres.subList(0, 2)).build();
        assertEquals(newFilm.getGenres(), genreDbStorage.getFilmsGenres(1));
    }

}
