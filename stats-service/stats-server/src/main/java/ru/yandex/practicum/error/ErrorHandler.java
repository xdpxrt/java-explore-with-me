package ru.yandex.practicum.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.error.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.Arrays;

import static ru.yandex.practicum.util.Constants.FORMATTER;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(ValidationException e) {
        return new ApiError(e.getMessage(), "Incorrect request", HttpStatus.BAD_REQUEST.name(),
                LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleServerException(RuntimeException e) {
        return new ApiError(e.getMessage(), Arrays.toString(e.getStackTrace()), HttpStatus.INTERNAL_SERVER_ERROR.name(),
                LocalDateTime.now().format(FORMATTER));
    }
}