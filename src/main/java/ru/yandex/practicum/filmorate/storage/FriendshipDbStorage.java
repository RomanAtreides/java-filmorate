package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class FriendshipDbStorage implements FriendshipStorage{
    private final JdbcTemplate jdbcTemplate;

    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addFriend(Long userId, Long friendId) {
        String sqlQuery = "INSERT INTO friendship (user_id, friend_id) VALUES (?, ?)";

        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void removeFriend(User user, User friend) {
        String sqlQuery = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";

        jdbcTemplate.update(sqlQuery, user.getId(), friend.getId());
    }
}
