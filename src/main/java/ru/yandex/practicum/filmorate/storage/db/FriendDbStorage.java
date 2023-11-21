package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private List<User> processFriendsQuery(String sql, Object[] params) {
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> User.builder()
                .id(rs.getInt("user_id"))
                .email(rs.getString("user_email"))
                .login(rs.getString("user_login"))
                .name(rs.getString("user_name"))
                .birthday(rs.getDate("user_birthday").toLocalDate())
                .friendsId(getFriendsIds(rs.getInt("user_id")))
                .build());
    }

    @Override
    public List<User> getFriendsList(int id) {
        String sqlGetId = "SELECT u.user_id, u.user_email, u.user_login, u.user_name, u.user_birthday " +
                "FROM users u " +
                "JOIN friends f ON u.user_id = f.friend_id " +
                "WHERE f.user_id = ?";
        return processFriendsQuery(sqlGetId, new Object[]{id});
    }

    @Override
    public List<User> getSameFriends(int id, int otherId) {
        String sqlGetSame = "SELECT u.user_id, u.user_email, u.user_login, u.user_name, u.user_birthday " +
                "FROM users u " +
                "JOIN friends f1 ON u.user_id = f1.friend_id " +
                "JOIN friends f2 ON f1.friend_id = f2.friend_id " +
                "WHERE f1.user_id = ? AND f2.user_id = ?";
        return processFriendsQuery(sqlGetSame, new Object[]{id, otherId});
    }

    private Set<Integer> getFriendsIds(int userId) {
        String sqlGetFriendIds = "SELECT friend_id FROM friends WHERE user_id = ?";
        List<Integer> friendIds = jdbcTemplate.queryForList(sqlGetFriendIds, Integer.class, userId);
        return new HashSet<>(friendIds);
    }


}
