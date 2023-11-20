package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void like(int userId, int filmId) {
        String sql = "INSERT INTO likes (user_id, film_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, filmId);
    }

    @Override
    public void unlike(int userId, int filmId) {
        String sql = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";
        jdbcTemplate.update(sql, userId, filmId);
    }

    public List<Film> getPopular(int count) {
        String sql = "SELECT f.film_id, f.film_name, f.film_release_date, f.film_description, f.film_duration, f.film_rate, f.film_mpa_id, COUNT(l.film_id) AS likeCount " + "FROM films f " + "LEFT JOIN likes l ON f.film_id = l.film_id " + "GROUP BY f.film_id " + "ORDER BY likeCount DESC " + "LIMIT ?";
        return jdbcTemplate.query(sql, new Object[]{count}, FilmDbStorage::mapRowToFilm);
    }

    public Map<Integer, List<Integer>> getTopFilmsGenres(int filmCount) {
        Map<Integer, List<Integer>> filmGenresMap = new HashMap<>();

        String sql = "SELECT film_id, ARRAY_AGG(genre_id) AS genreIds " + "FROM film_genres " + "GROUP BY film_id " + "ORDER BY (SELECT COUNT(*) FROM likes WHERE likes.film_id = film_genres.film_id) DESC " + "LIMIT ?";

        jdbcTemplate.query(sql, new Object[]{filmCount}, rs -> {
            while (rs.next()) {
                int filmId = rs.getInt("film_id");
                Array genreIdsArray = rs.getArray("genreIds");
                if (genreIdsArray != null) {
                    List<Integer> genreIds = Arrays.asList((Integer[]) genreIdsArray.getArray());
                    filmGenresMap.put(filmId, genreIds);
                } else {
                    filmGenresMap.put(filmId, Collections.emptyList());
                }
            }
        });
        return filmGenresMap;
    }

    public Map<Integer, List<Integer>> getAllFilmsGenres() {
        Map<Integer, List<Integer>> filmGenresMap = new HashMap<>();
        String sql = "SELECT film_id, ARRAY_AGG(genre_id) AS genreIds " + "FROM film_genres " + "GROUP BY film_id " + "ORDER BY (SELECT COUNT(*) FROM likes WHERE likes.film_id = film_genres.film_id) DESC";
        jdbcTemplate.query(sql, rs -> {
            while (rs.next()) {
                int filmId = rs.getInt("film_id");
                Array genreIdsArray = rs.getArray("genreIds");
                if (genreIdsArray != null) {
                    Object[] genreIdsObj = (Object[]) genreIdsArray.getArray();
                    List<Integer> genreIds = Arrays.stream(genreIdsObj).map(obj -> Integer.valueOf(String.valueOf(obj))).collect(Collectors.toList());
                    filmGenresMap.put(filmId, genreIds);
                } else {
                    filmGenresMap.put(filmId, Collections.emptyList());
                }
            }
        });
        return filmGenresMap;
    }

}
