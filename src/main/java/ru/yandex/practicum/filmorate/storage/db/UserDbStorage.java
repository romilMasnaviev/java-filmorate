package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        validateUser(user);
        String sqlInsert = "INSERT INTO users (user_email, user_login, user_name, user_birthday) VALUES (?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sqlInsert,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        if (rowsAffected > 0) {
            String sqlSelect = "SELECT TOP 1 user_id FROM users ORDER BY user_id DESC";
            Integer userId = jdbcTemplate.queryForObject(sqlSelect, Integer.class);
            if (userId != null) {
                user.setId(userId);
                return user;
            }

        }
        throw new RuntimeException("Failed to add new user");
    }

    @Override
    public User updateUser(User user) {
        validateUser(user);
        if (getUser(user.getId()) == null) {
            throw new NotFoundException("Failed to update user with id " + user.getId());
        }
        String sqlUpdate = "UPDATE users SET user_email = ?, user_login = ?, user_name = ?, user_birthday = ? WHERE user_id = ?";
        int rowsAffected = jdbcTemplate.update(
                sqlUpdate,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        if (rowsAffected > 0) {
            return user;
        } else {
            throw new NotFoundException("Failed to update user with id " + user.getId());
        }
    }

    @Override
    public User deleteUser(int userId) {
        User user = getUser(userId);
        if (user != null) {
            String sqlDelete = "DELETE FROM users WHERE user_id = ?";
            jdbcTemplate.update(sqlDelete, userId);
            return user;
        } else {
            throw new NotFoundException("User with this id does not exist");
        }
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                User.builder()
                        .id(rs.getInt("user_id"))
                        .email(rs.getString("user_email"))
                        .login(rs.getString("user_login"))
                        .name(rs.getString("user_name"))
                        .birthday(rs.getDate("user_birthday")
                                .toLocalDate()).build());
    }

    @Override
    public User getUser(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        List<User> users = jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> User.builder()
                .id(rs.getInt("user_id"))
                .email(rs.getString("user_email"))
                .login(rs.getString("user_login"))
                .name(rs.getString("user_name"))
                .birthday(rs.getDate("user_birthday").toLocalDate())
                .build());
        if (users.size() == 1) {
            return users.get(0);
        } else {
            throw new NotFoundException("Failed to update user with id ");
        }
    }

    @Override
    public boolean containsUser(int userId) {
        return getUser(userId) != null;
    }

    private void validateUser(User user) {
        isValidMail(user);
        isValidLogin(user);
        isValidBirthday(user);
        isNullName(user);
    }

    private void isNullName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    private void isValidMail(User user) {
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("The email does not contain @");
        }
    }

    private void isValidLogin(User user) {
        if (user.getLogin().contains(" ") | user.getLogin().isEmpty()) {
            throw new ValidationException("The login contains a space, or it is empty");
        }
    }

    private void isValidBirthday(User user) {
        if (!user.getBirthday().isBefore(LocalDate.now())) {
            throw new ValidationException("The birthday later today");
        }
    }
}
