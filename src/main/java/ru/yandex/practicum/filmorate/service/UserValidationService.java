package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidationService {
    public boolean validate(User user) {
        boolean isValid = true;
        int loginLinesNumber = user.getLogin().split(" ").length;

        if (user.getId() < 0) {
            log.warn("Попытка добавить пользователя с отрицательным id {}", user.getId());
            throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "id не может быть отрицательным!");
        }

        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Попытка добавить пользователя с неверно указанной почтой - {}", user.getEmail());
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Не указана электронная почта!");
        }

        if (user.getLogin().isBlank() || loginLinesNumber > 1) {
            log.warn("Попытка добавить пользователя с неверным логином - {}", user.getLogin());
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Логин указан неверно!");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Попытка добавить пользователя с неверной датой рождения - {}", user.getBirthday());
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Дата рождения указана неверно!");
        }

        if (user.getName().isBlank()) {
            log.info("Попытка добавить пользователя с пустым именем. Имя будет заменено на логин - {}",
                    user.getLogin());
            user.setName(user.getLogin());
        }
        return isValid;
    }
}
