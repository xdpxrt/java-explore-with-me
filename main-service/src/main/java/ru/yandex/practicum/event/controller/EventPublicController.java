package ru.yandex.practicum.event.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.event.dto.FullEventDTO;
import ru.yandex.practicum.event.dto.ShortEventDTO;
import ru.yandex.practicum.event.service.EventService;
import ru.yandex.practicum.event.state.EventSort;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.yandex.practicum.util.Constants.DATE_FORMAT;
import static ru.yandex.practicum.util.Constants.EVENTS_PUBLIC_URI;
import static ru.yandex.practicum.util.Utilities.FromSizePage;

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
                                                  @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangEnd,
                                                  @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                  @RequestParam EventSort eventSort,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Response from GET request on {}", EVENTS_PUBLIC_URI);
        return eventService.getPublishedEvents(text, categories, paid, rangeStart, rangEnd, onlyAvailable, eventSort,
                FromSizePage(from, size));
    }

    @GetMapping(ID_URI)
    public FullEventDTO getPublishedEventById(@PathVariable @Positive Long id) {
        log.info("Response from GET request on {}", EVENTS_PUBLIC_URI + ID_URI);
        return eventService.getPublishedEventById(id);
    }
}