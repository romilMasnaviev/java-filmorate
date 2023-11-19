package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre getGenre(int genreId) {
        String sql = "SELECT * FROM genres WHERE genre_id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, new Object[]{genreId}, (rs, rowNum) ->
                Genre.builder()
                        .id(rs.getInt("genre_id"))
                        .name(rs.getString("genre_name"))
                        .build());
        if (genres.size() == 1) {
            return genres.get(0);
        } else {
            throw new NotFoundException("Failed to get genre with id ");
        }
    }

    public List<Genre> getAllGenres() {
        String sql = "SELECT * FROM genres";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                Genre.builder()
                        .id(rs.getInt("genre_id"))
                        .name(rs.getString("genre_name"))
                        .build());
    }

    public List<Genre> addGenreToFilm(List<Genre> genres, int filmId) {
        List<Integer> genreIds = new ArrayList<>();
        for (Genre genre : genres) {
            genreIds.add(genre.getId());
        }
        String sql = "INSERT INTO film_genres (film_id , genre_id) VALUES ( ? , ?)";
        for (Integer id : genreIds) {
            jdbcTemplate.update(sql, filmId, id);
        }
        return getFilmsGenres(filmId);
    }

    public List<Genre> getFilmsGenres(int filmId) {
        String sql = "SELECT g.genre_id, g.genre_name " +
                "FROM genres g " +
                "INNER JOIN film_genres fg ON g.genre_id = fg.genre_id " +
                "WHERE fg.film_id = ?";

        return jdbcTemplate.query(sql, new Object[]{filmId}, (rs, rowNum) ->
                Genre.builder()
                        .id(rs.getInt("genre_id"))
                        .name(rs.getString("genre_name")).build());
    }

    @Override
    public List<Genre> update(Film newFilm) {
        Set<Genre> uniqueGenres = new LinkedHashSet<>(newFilm.getGenres());
        List<Genre> genres = new ArrayList<>(uniqueGenres);
        String deleteSql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(deleteSql, newFilm.getId());
        String insertSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        for (Genre genre : genres) {
            if (genre.getId() != 0) {
                jdbcTemplate.update(insertSql, newFilm.getId(), genre.getId());
            }

        }
        return getFilmsGenres(newFilm.getId());
    }
}
