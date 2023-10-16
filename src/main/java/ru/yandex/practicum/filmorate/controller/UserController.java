package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@RestController()
@RequestMapping("/users")
public class UserController {

    public UserStorage userStorage;

    @Autowired
    public UserController(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("Received request to add new user");
        return userStorage.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Received request to update user");
        return userStorage.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Received request to get all users");
        return ((InMemoryUserStorage) userStorage).getAllUsers();
    }

}
