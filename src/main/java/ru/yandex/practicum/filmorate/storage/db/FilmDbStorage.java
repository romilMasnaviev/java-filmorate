package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryFilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FilmDbStorage extends InMemoryFilmStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("film_name"))
                .releaseDate(rs.getDate("film_release_date").toLocalDate())
                .description(rs.getString("film_description"))
                .duration(rs.getInt("film_duration"))
                .rate(rs.getInt("film_rate"))
                .mpa(Mpa.builder().id(rs.getInt("film_mpa_id")).build())
                .build();
    }

    @Override
    public Film addFilm(Film film) {
        validateFilm(film);
        String sql = "INSERT INTO films ( film_name, film_release_date, film_description, film_duration, film_rate, film_mpa_id) VALUES (?, ?, ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update((con) -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"film_id"});
            ps.setString(1, film.getName());
            ps.setDate(2, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setString(3, film.getDescription());
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getRate());
            ps.setInt(6, film.getMpa().getId());
            return ps;
        }, keyHolder);
        if (rowsAffected > 0) {
            if (keyHolder.getKey() != null) {
                film.setId(keyHolder.getKey().intValue());
                return film;
            }
        }
        throw new RuntimeException("Failed to add new film");
    }

    @Override
    public Film updateFilm(Film film) {
        validateFilm(film);
        if (getFilm(film.getId()) == null) {
            throw new NotFoundException("Failed to update user with id " + film.getId());
        }
        String sql = "UPDATE films SET film_name = ? , film_release_date = ?," +
                "film_description = ? , film_duration = ? , film_rate = ? , film_mpa_id = ? WHERE film_id = ?";
        int rowsEffected = jdbcTemplate.update(
                sql,
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());
        if (rowsEffected > 0) {
            return film;
        } else {
            throw new NotFoundException("Failed to update user with id " + film.getId());
        }
    }

    @Override
    public Film deleteFilm(int filmId) {
        Film film = getFilm(filmId);
        if (film != null) {
            String sqlDelete = "DELETE FROM users WHERE user_id = ?";
            jdbcTemplate.update(sqlDelete, filmId);
            return film;
        } else {
            throw new NotFoundException("There is not this film in storage");
        }
    }

    @Override
    public Film getFilm(int filmId) {
        String sql = "SELECT * FROM films WHERE film_id = ?";
        List<Film> films = jdbcTemplate.query(sql, new Object[]{filmId}, FilmDbStorage::mapRowToFilm);
        if (films.size() == 1) {
            return films.get(0);
        } else {
            throw new NotFoundException("Failed to update film with id ");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT * FROM films ";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                Film.builder()
                        .id(rs.getInt("film_id"))
                        .name(rs.getString("film_name"))
                        .releaseDate(rs.getDate("film_release_date").toLocalDate())
                        .description(rs.getString("film_description"))
                        .duration(rs.getInt("film_duration"))
                        .rate(rs.getInt("film_rate"))
                        .mpa(Mpa.builder()
                                .id(rs.getInt("film_mpa_id"))
                                .build())
                        .build());
    }

    @Override
    public boolean containsFilm(int filmId) {
        return getFilm(filmId) != null;
    }

}
