package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

public interface FriendshipStorage {
    void addFriend(Long userId, Long friendId);
}
