package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.List;
import java.util.Set;

@Component
public class FriendDbStorage implements FriendStorage {

    JdbcTemplate jdbcTemplate;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(int id, int friendId) {

    }

    @Override
    public void removeFriend(int id, int friendId) {

    }

    @Override
    public List<User> getFriendsList(int id) {
        return null;
    }

    @Override
    public Set<User> getSameFriends(int id, int otherId) {
        return null;
    }
}
