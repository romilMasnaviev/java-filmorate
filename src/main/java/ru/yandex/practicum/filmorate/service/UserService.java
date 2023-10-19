package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public void addFriend(int id, int friendId) {
        if (id < 1 | friendId < 1) {
            throw new NotFoundException("wrong friend id");
        }
        if (storage.containsUser(id) || storage.containsUser(friendId)) {
            storage.getUser(id).getFriendsId().add(friendId);
            storage.getUser(friendId).getFriendsId().add(id);
        }
    }

    public void updateFriends() {
        for (User user : storage.getAllUsers()) {
            if (user.getFriendsId() == null) {
                user.setFriendsId(new HashSet<>());
            }
            Set<Integer> userFriends = user.getFriendsId();

            for (Integer friendsId : userFriends) {
                storage.getUser(friendsId).getFriendsId().add(user.getId());
            }
        }
    }

    public void removeFriend(int id, int friendId) {
        if (storage.containsUser(id)) {
            storage.getUser(id).getFriendsId().remove(friendId);
        }
        if (storage.containsUser(friendId)) {
            storage.getUser(friendId).getFriendsId().remove(id);
        }
    }

    public List<User> getFriends(int id) {
        Set<User> friendsList = new HashSet<>();
        if (storage.containsUser(id)) {
            for (Integer userId : storage.getUser(id).getFriendsId()) {
                friendsList.add(storage.getUser(userId));
            }
        }
        return friendsList.stream().sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());
    }

    public Set<User> getSameFriends(int id, int otherId) {
        if (storage.containsUser(id) & storage.containsUser(otherId)) {
            return getFriends(id).stream()
                    .filter(getFriends(otherId)::contains)
                    .collect(Collectors.toSet());
        } else throw new ValidationException("Some of the friends are not there");

    }
}
