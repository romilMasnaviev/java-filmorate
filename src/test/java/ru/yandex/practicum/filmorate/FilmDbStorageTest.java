package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private FilmDbStorage filmDbStorage;

    @BeforeEach
    void setup() {
        filmDbStorage = new FilmDbStorage(jdbcTemplate);
    }

    @Test
    void testAddFilm() {
        Film film = Film.builder().name("nisi eiusmod").description("adipisicing").duration(100).releaseDate(LocalDate.of(1967, 3, 25)).mpa(Mpa.builder().id(1).build()).build();
        Film savedFilm = filmDbStorage.addFilm(film);
        Film retrievedFilm = filmDbStorage.getFilm(savedFilm.getId());
        assertThat(savedFilm).isNotNull();
        assertThat(savedFilm.getId()).isPositive();
        assertThat(retrievedFilm).usingRecursiveComparison().isEqualTo(film);
    }

    @Test
    void testAddInvalidFilm() {
        Film invalidFilm = Film.builder().name("").description("Description").duration(200).releaseDate(LocalDate.of(1900, 3, 25)).mpa(Mpa.builder().id(1).build()).build();
        assertThrows(ValidationException.class, () -> filmDbStorage.addFilm(invalidFilm));
    }

    @Test
    void testAddFilmWithInvalidReleaseDate() {
        Film invalidFilm = Film.builder().name("Name").description("Description").duration(200).releaseDate(LocalDate.of(1890, 3, 25)).mpa(Mpa.builder().id(1).build()).build();
        assertThrows(ValidationException.class, () -> filmDbStorage.addFilm(invalidFilm));
    }

    @Test
    void testUpdateFilm() {
        Film initialFilm = Film.builder().name("Initial Film").description("Initial description").releaseDate(LocalDate.of(1989, 4, 17)).duration(200).mpa(Mpa.builder().id(1).build()).build();
        Film savedInitialFilm = filmDbStorage.addFilm(initialFilm);
        Film updatedFilm = Film.builder().id(savedInitialFilm.getId()).name("Film Updated").description("New film updated description").releaseDate(LocalDate.of(1989, 4, 17)).duration(190).rate(4).mpa(Mpa.builder().id(2).build()).build();
        Film savedUpdatedFilm = filmDbStorage.updateFilm(updatedFilm);
        assertEquals(updatedFilm, savedUpdatedFilm);
    }



}
