package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController()
@RequestMapping("/users")
public class UserController {

    private final UserStorage storage;
    private final UserService service;

    @Autowired
    public UserController(UserStorage storage, UserService service) {
        this.storage = storage;
        this.service = service;
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("Received request to add new user");
        User newUser = storage.addUser(user);
        service.updateFriends();
        return newUser;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Received request to update user");
        User newUser = storage.updateUser(user);
        service.updateFriends();
        return newUser;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Received request to get all users");
        return storage.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        return storage.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Received request to add friend");
        service.addFriend(id, friendId);
        service.updateFriends();
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Received request to remove friend");
        service.removeFriend(id, friendId);
        service.updateFriends();
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendsList(@PathVariable int id) {
        log.info("Received request to get friends list");
        return service.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getSameFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Received request to get same friend list");
        return service.getSameFriends(id, otherId);
    }

}
