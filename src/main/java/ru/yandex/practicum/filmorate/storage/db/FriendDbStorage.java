package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.List;

@Component
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(int id, int friendId) {
        String sqlAdd = "INSERT INTO friends (user_id , friend_id) VALUES ( ?, ?)";
        jdbcTemplate.update(sqlAdd, id, friendId);
    }

    @Override
    public void removeFriend(int id, int friendId) {
        String sqlDelete = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlDelete, id, friendId);
    }

    @Override
    public List<Integer> getFriendsList(int id) {
        String sqlGetId = "SELECT friend_id FROM friends WHERE user_id = ?";
        return jdbcTemplate.queryForList(sqlGetId, Integer.class, id);
    }

    @Override
    public List<Integer> getSameFriends(int id, int otherId) {
        String sqlGetSame = "SELECT f1.friend_id " +
                "FROM friends f1 " +
                "JOIN friends f2 ON f1.friend_id = f2.friend_id " +
                "WHERE f1.user_id = ? AND f2.user_id = ?";
        return jdbcTemplate.queryForList(sqlGetSame, Integer.class, id, otherId);
    }
}
