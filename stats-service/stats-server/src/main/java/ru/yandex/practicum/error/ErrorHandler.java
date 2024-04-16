package ru.yandex.practicum.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.error.exceptions.ValidationException;

import java.time.LocalDateTime;

import static ru.yandex.practicum.util.Constants.FORMATTER;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(ValidationException e) {
        log.debug("Получен статус 400 Bad Request {}", e.getMessage(), e);
        return new ApiError(e.getMessage(), "Incorrect request", HttpStatus.BAD_REQUEST.name(),
                LocalDateTime.now().format(FORMATTER));
    }
}
