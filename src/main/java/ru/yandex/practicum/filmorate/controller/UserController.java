package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmorateStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController()
@RequestMapping("/users")
public class UserController extends FilmorateStorage {

    @PostMapping
    public User addUser(@RequestBody String gsonString) {
        log.info("Received request to add new user");
        User userWithId = validateUser(gsonString).toBuilder().id(id++).build();
        filmorateStorageHashMap.put(userWithId.getId(), userWithId);
        log.info("User added successfully: {}", userWithId);
        return userWithId;
    }

    @PutMapping
    public User updateUser(@RequestBody String gsonString) {
        log.info("Received request to update user");
        User newUser = validateUser(gsonString);
        if (filmorateStorageHashMap.containsKey(newUser.getId())) {
            filmorateStorageHashMap.put(newUser.getId(), newUser);
            log.info("User updated successfully: {}", newUser);
            return newUser;
        } else {
            log.error("Failed to update User: User with ID {} doesnt exists", newUser.getId());
            throw new NotFoundException("There is no such user in the library");
        }
    }

    @GetMapping
    public List<Object> getAllUsers() {
        log.info("Received request to get all users");
        return new ArrayList<>(filmorateStorageHashMap.values());
    }

    private User validateUser(String gsonString) {
        User user = gson.fromJson(gsonString, User.class);
        isValidMail(user);
        isValidLogin(user);
        isValidBirthday(user);
        isNullName(user);
        return user;
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
        if ((user.getLogin().contains(" ") | user.getLogin().isEmpty())) {
            throw new ValidationException("The login contains a space, or it is empty");
        }
    }

    private void isValidBirthday(User user) {
        if (!user.getBirthday().isBefore(LocalDate.now())) {
            throw new ValidationException("The birthday later today");
        }
    }

}
