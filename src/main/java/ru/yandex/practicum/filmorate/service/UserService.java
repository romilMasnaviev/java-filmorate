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
        if (userStorage.containsUser(id) && userStorage.containsUser(friendId)){
            friendStorage.addFriend(id,friendId);
        }
        // TODO friendStorage
    }

    public void removeFriend(int id, int friendId) {
        if (id < 1 | friendId < 1) {
            throw new NotFoundException("wrong friend id");
        }
        if (userStorage.containsUser(id) && userStorage.containsUser(friendId)){
            friendStorage.removeFriend(id,friendId);
        }
    }

    public List<User> getFriends(int id) {
        // TODO friendStorage
        List<Integer> friendsId = new ArrayList<>();
        if (id < 1) {
            throw new NotFoundException("wrong friend id");
        }
        if (userStorage.containsUser(id)){
            friendsId =friendStorage.getFriendsList(id);
        }
        List<User> friends = new ArrayList<>();
        for (Integer friendId: friendsId) {
            friends.add(userStorage.getUser(friendId));
        }
        return friends;
    }

    public List<User> getSameFriends(int id, int otherId) {
        List<Integer> friendsId = new ArrayList<>();
        if (id < 1) {
            throw new NotFoundException("wrong friend id");
        }
        if (userStorage.containsUser(id)){
            friendsId =friendStorage.getSameFriends(id,otherId);
        }
        List<User> friends = new ArrayList<>();
        for (Integer friendId: friendsId) {
            friends.add(userStorage.getUser(friendId));
        }
        return friends;
    }

    public User addUser(User user) {
        User newUser = userStorage.addUser(user);
        return newUser;
    }

    public User updateUser(User user) throws Exception {
        User newUser = userStorage.updateUser(user);
        return newUser;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUser(int id) {
        return userStorage.getUser(id);
    }
}
