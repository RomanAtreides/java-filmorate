package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
	private final UserDbStorage userStorage;

	@Test
	public void testFindUserById() {
		Optional<User> userOptional = Optional.ofNullable(userStorage.findUserById(1L));

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
				);
	}

	@Test
	public void testCreate() {
		User newUser01 = new User(
				1,
				"new@email",
				"new_login",
				LocalDate.of(1991, 1, 1),
				"new_name"
		);

		int usersCountBefore = userStorage.findAll().size();

		log.info("количество пользователей до добавления = " + usersCountBefore);
		userStorage.create(newUser01);

		int usersCountAfter = userStorage.findAll().size();

		log.info("количество пользователей после добавления = " + usersCountAfter);
		assertThat(usersCountAfter).isEqualTo(usersCountBefore + 1);
	}

	@Test
	public void testPut() {
		User user02 = new User(
				2,
				"old@email_02",
				"old_login_02",
				LocalDate.of(1992, 2, 2),
				"old_name_02"
		);

		User newUser02 = new User(
				2,
				"new@email_03",
				"old_login_02",
				LocalDate.of(1992, 2, 2),
				"old_name_02"
		);

		userStorage.create(user02);

		int usersCountBefore = userStorage.findAll().size();

		log.info("количество пользователей до добавления = " + usersCountBefore);
		assertThat(userStorage.findAll()).anyMatch(user -> user.getEmail().equals("old@email_02"));
		userStorage.put(newUser02);

		int usersCountAfter = userStorage.findAll().size();

		log.info("количество пользователей после добавления = " + usersCountAfter);
		assertThat(usersCountBefore).isEqualTo(usersCountAfter);
		assertThat(userStorage.findAll()).anyMatch(user -> user.getEmail().equals("new@email_03"));
	}
}