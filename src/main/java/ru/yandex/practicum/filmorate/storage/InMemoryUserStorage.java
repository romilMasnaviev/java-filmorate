package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Integer, User> userStorage = new HashMap<>();
    private int id = 1;

    public User addUser(User user) {
        User userWithId = validateUser(user).toBuilder().id(id++).build();
        userStorage.put(userWithId.getId(), userWithId);
        return userWithId;
    }

    public User updateUser(User user) {
        User newUser = validateUser(user);
        if (userStorage.containsKey(newUser.getId())) {
            userStorage.put(newUser.getId(), newUser);
            return newUser;
        } else {
            throw new NotFoundException("There is no such user in the library");
        }
    }

    @Override
    public User deleteUser(User userId) {
        //TODO
        return null;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userStorage.values());
    }

    private User validateUser(User user) {
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
