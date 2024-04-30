package ru.yandex.practicum.event.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.error.exception.ValidationException;
import ru.yandex.practicum.event.dto.FullEventDTO;
import ru.yandex.practicum.event.dto.ShortEventDTO;
import ru.yandex.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.yandex.practicum.util.Constants.*;
import static ru.yandex.practicum.util.Utilities.fromSizePage;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(EVENTS_PUBLIC_URI)
public class EventPublicController {
    public static final String ID_URI = "/{id}";
    private final EventService eventService;

    @GetMapping
    public List<ShortEventDTO> getPublishedEvents(@RequestParam(defaultValue = "") String text,
                                                  @RequestParam(defaultValue = "") List<Long> categories,
                                                  @RequestParam(required = false) Boolean paid,
                                                  @RequestParam(required = false)
                                                  @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeStart,
                                                  @RequestParam(required = false)
                                                  @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeEnd,
                                                  @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                  @RequestParam(required = false) EventSort sort,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = "10") @Positive int size,
                                                  HttpServletRequest request) {
        log.info("Response from GET request on {}", EVENTS_PUBLIC_URI);
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd))
            throw new ValidationException("The date of start cannot be after end");
        return eventService.getPublishedEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                fromSizePage(from, size), request);
    }

    @GetMapping(ID_URI)
    public FullEventDTO getPublishedEventById(@PathVariable @Positive Long id, HttpServletRequest request) {
        log.info("Response from GET request on {}", EVENTS_PUBLIC_URI + ID_URI);
        return eventService.getPublishedEventById(id, request);
    }
}