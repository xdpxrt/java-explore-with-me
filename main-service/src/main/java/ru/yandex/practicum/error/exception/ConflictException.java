package ru.yandex.practicum.error.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(final String massage) {
        super(massage);
    }
}