package ru.yandex.practicum.error.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String massage) {
        super(massage);
    }
}