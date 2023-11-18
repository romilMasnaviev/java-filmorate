package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user) throws Exception;

    User deleteUser(int userId);

    List<User> getAllUsers();

    User getUser(int userId);

    boolean containsUser(int userId);
}
