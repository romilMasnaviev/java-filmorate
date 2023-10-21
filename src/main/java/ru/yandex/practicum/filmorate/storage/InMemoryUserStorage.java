package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Integer, User> storage = new HashMap<>();
    private int id = 1;

    @Override
    public User addUser(User user) {
        User userWithId = validateUser(user).toBuilder().id(id++).friendsId(new HashSet<>()).build();
        storage.put(userWithId.getId(), userWithId);
        return userWithId;
    }

    @Override
    public User updateUser(User user) {
        User newUser = validateUser(user);
        if (newUser.getFriendsId() == null) {
            newUser.setFriendsId(new HashSet<>());
        }
        if (storage.containsKey(newUser.getId())) {
            storage.put(newUser.getId(), newUser);
            return newUser;
        } else {
            throw new NotFoundException("There is no such user in the library");
        }
    }

    @Override
    public User deleteUser(int userId) {
        return storage.remove(userId);
    }

    @Override
    public User getUser(int userId) {
        if (storage.containsKey(userId)) {
            return storage.get(userId);
        } else throw new NotFoundException("User not found");
    }

    @Override
    public boolean containsUser(int userId) {
        return storage.containsKey(userId);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(storage.values());
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
