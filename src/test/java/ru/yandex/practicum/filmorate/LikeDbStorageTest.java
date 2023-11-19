package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    Film film1 = Film.builder()
            .name("Inception")
            .description("A thief who steals corporate secrets through the use of dream-sharing technology")
            .releaseDate(LocalDate.of(2010, 7, 16))
            .duration(148)
            .mpa(Mpa.builder().id(1).build())
            .build();
    Film film2 = Film.builder()
            .name("The Shawshank Redemption")
            .description("Two imprisoned men bond over a number of years, finding solace and eventual redemption")
            .releaseDate(LocalDate.of(1994, 9, 10))
            .duration(142)
            .mpa(Mpa.builder().id(2).build())
            .build();
    User user1 = User.builder()
            .login("john_doe")
            .email("john@example.com")
            .name("John Doe")
            .birthday(LocalDate.of(1985, 5, 12))
            .build();
    User user2 = User.builder()
            .login("jane_doe")
            .email("jane@example.com")
            .name("Jane Doe")
            .birthday(LocalDate.of(1990, 8, 25))
            .build();

    private FilmDbStorage filmDbStorage;
    private LikeDbStorage likeDbStorage;
    private UserDbStorage userDbStorage;

    @BeforeEach
    void setup() {
        filmDbStorage = new FilmDbStorage(jdbcTemplate);
        likeDbStorage = new LikeDbStorage(jdbcTemplate);
        userDbStorage = new UserDbStorage(jdbcTemplate);
    }

    @Test
    void testMostPopularFilms() {
        filmDbStorage.addFilm(film1);
        filmDbStorage.addFilm(film2);
        userDbStorage.addUser(user1);
        userDbStorage.addUser(user2);

        //add 1 like film 1
        likeDbStorage.like(1,1);
        List<Film> topFilms = likeDbStorage.getPopular(3);

        assertEquals(topFilms.get(0).getId(), 1);

        //add 2 like film 2
        likeDbStorage.like(1,2);
        likeDbStorage.like(2,2);

        topFilms = likeDbStorage.getPopular(3);
        assertEquals(topFilms.get(0).getId(), 2);

        //remove 2 like film 2
        likeDbStorage.unlike(1,2);
        likeDbStorage.unlike(2,2);

        topFilms = likeDbStorage.getPopular(3);
        assertEquals(topFilms.get(0).getId(), 1);
    }

}
