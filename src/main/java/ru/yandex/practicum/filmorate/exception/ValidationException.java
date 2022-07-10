package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidationException extends ResponseStatusException {
    public ValidationException(HttpStatus status, String message) {
        super(status, message);
    }
}
