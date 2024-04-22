package ru.yandex.practicum.event.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.event.dto.FullEventDTO;
import ru.yandex.practicum.event.dto.NewEventDTO;
import ru.yandex.practicum.event.dto.UpdateEventDTO;
import ru.yandex.practicum.event.service.EventService;
import ru.yandex.practicum.request.dto.RequestDTO;
import ru.yandex.practicum.request.dto.RequestResultDTO;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.yandex.practicum.util.Constants.EVENTS_PRIVATE_URI;
import static ru.yandex.practicum.util.Constants.EVENT_ID_URI;
import static ru.yandex.practicum.util.Utilities.FromSizePage;
import static ru.yandex.practicum.util.Utilities.checkEventStart;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(EVENTS_PRIVATE_URI)
public class EventPrivateController {
    public static final String EVENT_ID_REQUESTS_URI = "/{eventId}/requests";
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FullEventDTO addEvent(@RequestBody @Valid NewEventDTO newEventDTO,
                                 @PathVariable @Positive Long userId) {
        log.info("Response from POST request on {}", EVENTS_PRIVATE_URI);
        checkEventStart(newEventDTO.getEventDate());
        return eventService.addEvent(newEventDTO, userId);
    }

    @GetMapping(EVENT_ID_URI)
    public FullEventDTO getEvent(@PathVariable @Positive Long userId,
                                 @PathVariable @Positive Long eventId) {
        log.info("Response from GET request on {}{}", EVENTS_PRIVATE_URI, EVENT_ID_URI);
        return eventService.getEvent(userId, eventId);
    }

    @GetMapping
    public List<FullEventDTO> getAllEvents(@PathVariable @Positive Long userId,
                                           @RequestParam @PositiveOrZero int from,
                                           @RequestParam @Positive int size) {
        log.info("Response from GET request on {}", EVENTS_PRIVATE_URI);
        return eventService.getAllEvents(userId, FromSizePage(from, size));
    }

    @PatchMapping(EVENT_ID_URI)
    public FullEventDTO updateEvent(@RequestBody @Valid UpdateEventDTO updateEventDTO,
                                    @PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long eventId) {
        log.info("Response from PATCH request on {}{}", EVENTS_PRIVATE_URI, EVENT_ID_URI);
        checkEventStart(updateEventDTO.getEventDate());
        return eventService.updateEvent(updateEventDTO, userId, eventId);
    }

    @GetMapping(EVENT_ID_REQUESTS_URI)
    public List<RequestDTO> getRequestsByEventId(@PathVariable @Positive Long userId,
                                                 @PathVariable @Positive Long eventId) {
        log.info("Response from GET request on {}{}", EVENTS_PRIVATE_URI, EVENT_ID_REQUESTS_URI);
        return eventService.getRequestsByEventId(userId, eventId);
    }

    @PatchMapping(EVENT_ID_REQUESTS_URI)
    public RequestResultDTO updateRequestsStatus(@RequestBody @Valid RequestResultDTO requestResultDTO,
                                                 @PathVariable @Positive Long userId,
                                                 @PathVariable @Positive Long eventId) {
        log.info("Response from PATCH request on {}{}", EVENTS_PRIVATE_URI, EVENT_ID_REQUESTS_URI);
        return eventService.updateRequestsStatus(requestResultDTO, userId, eventId);
    }
}