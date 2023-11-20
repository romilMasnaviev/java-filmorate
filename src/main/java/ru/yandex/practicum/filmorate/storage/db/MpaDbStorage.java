package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa getMpa(int mpaId) {
        String sql = "SELECT * FROM mpa WHERE mpa_id = ?";
        List<Mpa> mpa = jdbcTemplate.query(sql, new Object[]{mpaId}, (rs, rowNum) -> Mpa.builder().id(rs.getInt("mpa_id")).name(rs.getString("mpa_name")).build());
        if (mpa.size() == 1) {
            return mpa.get(0);
        } else {
            throw new NotFoundException("Failed to get mpa with id ");
        }
    }

    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, (rs, rowNum) -> Mpa.builder().id(rs.getInt("mpa_id")).name(rs.getString("mpa_name")).build());
    }
}
