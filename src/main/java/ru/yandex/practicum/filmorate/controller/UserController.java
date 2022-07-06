package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

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
        users.put(user.getEmail(), user);
        return user;
    }

    // Обновление пользователя
    @PutMapping
    public User put(@RequestBody User user) {
        users.put(user.getEmail(), user);
        return user;
    }
}
