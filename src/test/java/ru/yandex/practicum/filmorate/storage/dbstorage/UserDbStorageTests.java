package ru.yandex.practicum.filmorate.storage.dbstorage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTests {
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    @BeforeEach
    public void beforeEach() {
        User user01 = userStorage.findUserById(1L);
        User user02 = userStorage.findUserById(2L);
        User user03 = userStorage.findUserById(3L);

        friendshipStorage.removeFriend(user01, user02);
        friendshipStorage.removeFriend(user01, user03);
    }

    @Test
    public void shouldFindUserById() {
        long userId = 1L;
        Optional<User> userOptional = Optional.ofNullable(userStorage.findUserById(userId));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", userId));
    }

    @Test
    public void shouldCreateUserAndFindAllUsers() {
        User user04 = new User(
                4L,
                "user04@email",
                "usr04",
                LocalDate.of(1994, 4, 4),
                "user04",
                new Friendship(0, 0)
        );

        int usersCountBeforeAdd = userStorage.findAll().size();

        userStorage.create(user04);

        int usersCountAfterAdd = userStorage.findAll().size();

        assertThat(usersCountAfterAdd).isEqualTo(usersCountBeforeAdd + 1);
        assertThat(userStorage.findAll()).contains(user04);
    }

    @Test
    public void shouldUpdateUser() {
        User user01 = userStorage.findUserById(1L);

        User updatedUser01  = new User(
                1L,
                "new_user01@email",
                "usr01",
                LocalDate.of(1991, 1, 1),
                "user01",
                new Friendship(0, 0)
        );

        String userOldEmail = userStorage.findUserById(user01.getId()).getEmail();
        int usersCountBeforeUpdate = userStorage.findAll().size();

        assertThat(userOldEmail).isEqualTo("user01@email");
        userStorage.put(updatedUser01);

        String userNewEmail = userStorage.findUserById(updatedUser01.getId()).getEmail();
        int usersCountAfterUpdate = userStorage.findAll().size();

        assertThat(userNewEmail).isEqualTo("new_user01@email");
        assertThat(usersCountBeforeUpdate).isEqualTo(usersCountAfterUpdate);
    }

    @Test
    public void shouldFindUserFriends() {
        User user01 = userStorage.findUserById(1L);
        User user02 = userStorage.findUserById(2L);
        User user03 = userStorage.findUserById(3L);

        int userFriendsCountBeforeAdd = userStorage.findUserFriends(user01).size();

        friendshipStorage.addFriend(user01.getId(), user02.getId());
        friendshipStorage.addFriend(user01.getId(), user03.getId());

        List<User> userFriends = userStorage.findUserFriends(user01);
        int userFriendsCountAfterAdd = userFriends.size();

        assertThat(userFriendsCountAfterAdd).isEqualTo(userFriendsCountBeforeAdd + 2);
        assertThat(userFriends).contains(user02);
        assertThat(userFriends).contains(user03);
    }

    @Test
    public void shouldFindCommonFriends() {
        User user01 = userStorage.findUserById(1L);
        User user02 = userStorage.findUserById(2L);
        User user03 = userStorage.findUserById(3L);

        friendshipStorage.addFriend(user01.getId(), user03.getId());
        friendshipStorage.addFriend(user02.getId(), user03.getId());

        List<User> commonFriends = userStorage.findCommonFriends(user01, user02);

        assertThat(commonFriends).contains(user03);
        assertThat(commonFriends.size()).isEqualTo(1);
    }
}
