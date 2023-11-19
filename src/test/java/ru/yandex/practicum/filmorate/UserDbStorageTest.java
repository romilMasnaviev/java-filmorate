package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {


    private final JdbcTemplate jdbcTemplate;
    private UserDbStorage userDbStorage;

    @BeforeEach
    void setup() {
        userDbStorage = new UserDbStorage(jdbcTemplate);
    }

    @Test
    public void testAddUser() {
        User newUser = User.builder()
                .login("john_doe")
                .email("john@example.com")
                .name("John Doe")
                .birthday(LocalDate.of(1985, 5, 12))
                .build();
        User savedUser = userDbStorage.addUser(newUser);

        User retrievedUser = userDbStorage.getUser(savedUser.getId());

        assertThat(savedUser)
                .isNotNull();
        assertThat(savedUser.getId())
                .isPositive();
        assertThat(retrievedUser)
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void testAddUser_InvalidLogin_ShouldThrowRuntimeException() {
        User newUser = User.builder()
                .login("dolore ullamco")
                .email("yandex@mail.ru")
                .birthday(LocalDate.of(2446, 8, 20))
                .build();
        assertThrows(
                RuntimeException.class,
                () -> userDbStorage.addUser(newUser)
        );
    }

    @Test
    public void testAddUser_InvalidEmail_ShouldThrowRuntimeException() {
        User newUser = User.builder()
                .login("dolore ullamco")
                .email("mail.ru")
                .birthday(LocalDate.of(1980, 8, 20))
                .build();

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);

        assertThrows(
                RuntimeException.class,
                () -> userStorage.addUser(newUser)
        );
    }

    @Test
    public void testAddUser_InvalidData_ShouldThrowRuntimeException() {
        User newUser = User.builder()
                .login("dolore")
                .email("test@mail.ru")
                .birthday(LocalDate.of(2446, 8, 20))
                .build();
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        assertThrows(
                RuntimeException.class,
                () -> userStorage.addUser(newUser)
        );
    }


    @Test
    public void testAddAndUpdateUser() {
        User newUser = User.builder()
                .login("testUser")
                .email("test@example.com")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        User savedUser = userDbStorage.addUser(newUser);

        savedUser.setEmail("updatedTest@example.com");
        User updatedUser = userDbStorage.updateUser(savedUser);

        User retrievedUser = userDbStorage.getUser(savedUser.getId());
        assertThat(updatedUser).isNotNull();
        assertThat(retrievedUser).usingRecursiveComparison().isEqualTo(updatedUser);
    }

    @Test
    public void testUpdateUser_ShouldThrowException_ForNegativeId() {
        User updatedUser = User.builder()
                .id(-2)
                .login("doloreUpdate")
                .email("mail@yandex.ru")
                .name("est adipisicing")
                .birthday(LocalDate.of(1976, 9, 20))
                .build();
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        assertThrows(
                RuntimeException.class,
                () -> userStorage.updateUser(updatedUser)
        );
    }

    @Test
    public void testAddUserAndFindById() {
        User newUser = User.builder()
                .login("john_doe")
                .email("john@example.com")
                .name("John Doe")
                .birthday(LocalDate.of(1985, 5, 12))
                .build();
        User savedUser = userDbStorage.addUser(newUser);
        User retrievedUser = userDbStorage.getUser(savedUser.getId());
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isPositive();
        assertThat(retrievedUser).usingRecursiveComparison().isEqualTo(newUser);
    }

    @Test
    public void testUpdateUser_ShouldThrowNotFoundException_ForNegativeId() {
        User updatedUser = User.builder()
                .login("testUser")
                .email("test@example.com")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        userDbStorage.addUser(updatedUser);

        updatedUser.setId(-1);
        assertThrows(
                NotFoundException.class,
                () -> userDbStorage.updateUser(updatedUser)
        );
    }

    @Test
    public void testAddMultipleUsersAndRetrieveAll() {
        User user1 = User.builder()
                .login("user1")
                .email("user1@example.com")
                .name("User One")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        User user2 = User.builder()
                .login("user2")
                .email("user2@example.com")
                .name("User Two")
                .birthday(LocalDate.of(1995, 3, 15))
                .build();
        userDbStorage.addUser(user1);
        userDbStorage.addUser(user2);
        List<User> allUsers = userDbStorage.getAllUsers();
        assertThat(allUsers).isNotNull();
        assertThat(allUsers).hasSize(2);
        assertThat(allUsers).extracting("login").contains("user1", "user2");
    }
}