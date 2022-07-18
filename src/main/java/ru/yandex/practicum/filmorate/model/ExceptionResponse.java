package ru.yandex.practicum.filmorate.model;

public class ExceptionResponse {
    private final String exception;

    public ExceptionResponse(String exception) {
        this.exception = exception;
    }

    public String getException() {
        return exception;
    }
}
