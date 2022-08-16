package ru.yandex.practicum.filmorate.storage.dbstorage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendshipDbStorageTests {
    private final FriendshipStorage friendshipStorage;
    private final UserStorage userStorage;

    @Test
    public void shouldAddFriendAndRemoveFriend() {
        User newUser02 = new User(
                2,
                "new@email",
                "new_login",
                LocalDate.of(1992, 2, 2),
                "new_name",
                new Friendship(0, 0)
        );

        User newUser03 = new User(
                3,
                "new@email",
                "new_login",
                LocalDate.of(1993, 3, 3),
                "new_name",
                new Friendship(0, 0)
        );

        userStorage.create(newUser02);
        userStorage.create(newUser03);

        int userFriendsCountBeforeAdd = userStorage.findUserFriends(newUser02).size();

        assertThat(userFriendsCountBeforeAdd).isEqualTo(0);
        friendshipStorage.addFriend(newUser02.getId(), newUser03.getId());

        int userFriendsCountAfterAdd = userStorage.findUserFriends(newUser02).size();

        assertThat(userFriendsCountAfterAdd).isEqualTo(1);

        friendshipStorage.removeFriend(newUser02, newUser03);
        assertThat(userFriendsCountBeforeAdd).isEqualTo(0);
    }
}
