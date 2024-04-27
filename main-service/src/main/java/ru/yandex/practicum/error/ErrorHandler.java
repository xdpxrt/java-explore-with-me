package ru.yandex.practicum.error;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.error.exception.ConflictException;
import ru.yandex.practicum.error.exception.ForbiddenException;
import ru.yandex.practicum.error.exception.NotFoundException;
import ru.yandex.practicum.error.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.Arrays;

import static ru.yandex.practicum.util.Constants.FORMATTER;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(ValidationException e) {
        return new ApiError(HttpStatus.BAD_REQUEST.name(), "Incorrect request", e.getMessage(),
                LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleForbiddenException(ForbiddenException e) {
        return new ApiError(HttpStatus.FORBIDDEN.name(), "Incorrectly conditions fot this request",
                e.getMessage(), LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException e) {
        return new ApiError(HttpStatus.NOT_FOUND.name(), "The required object was not found", e.getMessage(),
                LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler({DataAccessException.class, ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(RuntimeException e) {
        return new ApiError(HttpStatus.CONFLICT.name(), "Incorrectly conditions fot this request",
                e.getMessage(), LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleServerException(RuntimeException e) {
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getMessage(), Arrays.toString(e.getStackTrace()),
                LocalDateTime.now().format(FORMATTER));
    }
}