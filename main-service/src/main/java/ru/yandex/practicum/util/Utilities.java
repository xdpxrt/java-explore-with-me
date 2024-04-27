package ru.yandex.practicum.util;

import org.springframework.data.domain.PageRequest;
import ru.yandex.practicum.error.exception.ConflictException;

import java.time.LocalDateTime;

public class Utilities {
    public static PageRequest fromSizePage(int from, int size) {
        return PageRequest.of(from / size, size);
    }

    public static void checkEventStart(LocalDateTime start) {
        if (start != null && start.isBefore(LocalDateTime.now().plusHours(2)))
            throw new ConflictException("The event can only start in two hours from now");
    }
}