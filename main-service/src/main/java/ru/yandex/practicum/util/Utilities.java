package ru.yandex.practicum.util;

import org.springframework.data.domain.PageRequest;
import ru.yandex.practicum.error.exception.ForbiddenException;

import java.time.LocalDateTime;

public class Utilities {
    public static PageRequest FromSizePage(int from, int size) {
        return PageRequest.of(from / size, size);
    }
    public static void checkEventStart(LocalDateTime start) {
        if (start.isBefore(LocalDateTime.now().plusHours(2)))
            throw new ForbiddenException("The event can only start in two hours from now");
    }
}