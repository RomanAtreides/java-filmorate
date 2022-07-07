package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    /*
     * Убедитесь, что созданные контроллеры соответствуют правилам REST.
     * Добавьте в классы-контроллеры эндпоинты с подходящим типом запроса для каждого из случаев:
     * создание пользователя;
     * обновление пользователя;
     * получение списка всех пользователей.
     * Эндпоинты для создания и обновления данных должны также вернуть созданную или изменённую сущность.
     */

    /*
     * Подсказка: про аннотацию @RequestBody.
     * Используйте аннотацию @RequestBody, чтобы создать объект из тела запроса на добавление или обновление сущности.
     */

    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    @Getter
    private final Map<String, User> users = new HashMap<>();

    // Получение списка всех пользователей
    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    /*
     * Проверьте данные, которые приходят в запросе на добавление нового фильма или пользователя.
     * Эти данные должны соответствовать определённым критериям:
     * электронная почта не может быть пустой и должна содержать символ @;
     * логин не может быть пустым и содержать пробелы;
     * имя для отображения может быть пустым — в таком случае будет использован логин;
     * дата рождения не может быть в будущем.
     */

    // Создание пользователя
    @PostMapping
    public User create(@RequestBody User user) {
        if (users.containsKey(user.getEmail())) {
            log.warn("Попытка добавить пользователя с уже существующей в базе почтой - {}", user.getEmail());
            throw new ValidationException("Такой пользователь уже зарегистрирован в системе!");
        }

        if (validate(user)) {
            users.put(user.getEmail(), user);
        }
        return user;
    }

    // Обновление пользователя
    @PutMapping
    public User put(@RequestBody User user) {
        if (validate(user)) {
            users.put(user.getEmail(), user);
        }
        return user;
    }

    private boolean validate(User user) {
        boolean isValid = true;

        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Попытка добавить пользователя в неверно указанной почтой - {}", user.getEmail());
            throw new ValidationException("Не указана электронная почта!");
        }

        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Попытка добавить пользователя с неверным логином - {}", user.getLogin());
            throw new ValidationException("Логин указан неверно!");
        }

        if (user.getName().isBlank()) {
            log.info("Попытка добавить пользователя с пустым именем. Имя будет заменено на логин - {}",
                    user.getLogin());
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Попытка добавить пользователя с неверной датой рождения - {}", user.getBirthday());
            throw new ValidationException("Дата рождения указана неверно!");
        }
        return isValid;
    }
}
