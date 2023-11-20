package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendStorageTest {

    private final JdbcTemplate jdbcTemplate;
    User user1 = User.builder().login("john_doe").email("john@example.com").name("John Doe").birthday(LocalDate.of(1985, 5, 12)).build();
    User user2 = User.builder().login("jane_doe").email("jane@example.com").name("Jane Doe").birthday(LocalDate.of(1990, 8, 25)).build();
    User user3 = User.builder().login("bob_smith").email("bob@example.com").name("Bob Smith").birthday(LocalDate.of(1982, 3, 17)).build();
    private UserDbStorage userDbStorage;
    private FriendDbStorage friendStorage;

    @BeforeEach
    void setup() {
        userDbStorage = new UserDbStorage(jdbcTemplate);
        friendStorage = new FriendDbStorage(jdbcTemplate);
    }

    @Test
    public void testAddFriendWithInvalidId() {
        userDbStorage.addUser(user1);
        userDbStorage.addUser(user2);
        userDbStorage.addUser(user3);
        assertThrows(RuntimeException.class, () -> friendStorage.addFriend(1, -1));
    }
}
