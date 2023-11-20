package ru.yandex.practicum.filmorate.storage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {

    void addFriend(int id, int friendId);

    void removeFriend(int id, int friendId);

    List<User> getFriendsList(int id);

    List<User> getSameFriends(int id, int otherId);
}
