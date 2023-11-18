package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface FriendStorage {

    void addFriend(int id, int friendId);

    void removeFriend(int id, int friendId);

    List<User> getFriendsList(int id);

    Set<User> getSameFriends(int id, int otherId);
}
