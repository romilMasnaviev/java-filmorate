package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage storage, FriendStorage friendStorage) {
        this.userStorage = storage;
        this.friendStorage = friendStorage;
    }

    public void addFriend(int id, int friendId) {
        if (id < 1 | friendId < 1) {
            throw new NotFoundException("wrong friend id");
        }
        if (userStorage.containsUser(id) && userStorage.containsUser(friendId)) {
            friendStorage.addFriend(id, friendId);
        }
    }

    public void removeFriend(int id, int friendId) {
        if (id < 1 | friendId < 1) {
            throw new NotFoundException("wrong friend id");
        }
        if (userStorage.containsUser(id) && userStorage.containsUser(friendId)) {
            friendStorage.removeFriend(id, friendId);
        }
    }

    public List<User> getFriends(int id) {
        return friendStorage.getFriendsList(id);
    }

    public List<User> getSameFriendsSet(int id, int otherId) {
        if (id < 1) {
            throw new NotFoundException("wrong friend id");
        }
        return friendStorage.getSameFriends(id,otherId);
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUser(int id) {
        return userStorage.getUser(id);
    }
}
