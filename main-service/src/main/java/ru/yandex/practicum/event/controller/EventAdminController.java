package ru.yandex.practicum.event.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.event.dto.FullEventDTO;
import ru.yandex.practicum.event.dto.UpdateEventDTO;
import ru.yandex.practicum.event.service.EventService;
import ru.yandex.practicum.event.state.EventState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.yandex.practicum.util.Constants.*;
import static ru.yandex.practicum.util.Utilities.FromSizePage;
import static ru.yandex.practicum.util.Utilities.checkEventStart;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(EVENTS_ADMIN_URI)
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    public List<FullEventDTO> getEventsByAdmin(@RequestParam(defaultValue = "") List<Long> usersIds,
                                               @RequestParam(defaultValue = "") List<EventState> statesIds,
                                               @RequestParam(defaultValue = "") List<Long> eventsIds,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeStart,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Response from GET request on {}", EVENTS_ADMIN_URI);
        return eventService.getEventsByAdmin(usersIds, statesIds, eventsIds, rangeStart, rangeEnd,
                FromSizePage(from, size));
    }

    @PatchMapping(EVENT_ID_URI)
    public FullEventDTO updateEventByAdmin(@RequestBody @Valid UpdateEventDTO updateEventDTO,
                                           @PathVariable Long eventId) {
        log.info("Response from GET request on {}", EVENTS_ADMIN_URI + EVENT_ID_URI);
        checkEventStart(updateEventDTO.getEventDate());
        return eventService.updateEventByAdmin(updateEventDTO, eventId);
    }
}
