package ru.yandex.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.event.dto.FullEventDTO;
import ru.yandex.practicum.event.dto.NewEventDTO;
import ru.yandex.practicum.event.dto.ShortEventDTO;
import ru.yandex.practicum.event.dto.UpdateEventDTO;
import ru.yandex.practicum.event.service.EventService;
import ru.yandex.practicum.request.dto.RequestDTO;
import ru.yandex.practicum.request.dto.RequestResultDTO;
import ru.yandex.practicum.request.dto.RequestUpdateDTO;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.yandex.practicum.util.Constants.*;
import static ru.yandex.practicum.util.Utilities.checkEventStart;
import static ru.yandex.practicum.util.Utilities.fromSizePage;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(EVENTS_PRIVATE_URI)
public class EventPrivateController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FullEventDTO addEvent(@RequestBody @Valid NewEventDTO newEventDTO,
                                 @PathVariable @Positive Long userId) {
        log.info("Response from POST request on {}", EVENTS_PRIVATE_URI);
        if (newEventDTO.getEventDate() != null) checkEventStart(newEventDTO.getEventDate());
        if (newEventDTO.getPaid() == null) newEventDTO.setPaid(false);
        if (newEventDTO.getParticipantLimit() == null) newEventDTO.setParticipantLimit(0L);
        if (newEventDTO.getRequestModeration() == null) newEventDTO.setRequestModeration(true);
        return eventService.addEvent(newEventDTO, userId);
    }

    @GetMapping(EVENT_ID_URI)
    public FullEventDTO getEvent(@PathVariable @Positive Long userId,
                                 @PathVariable @Positive Long eventId) {
        log.info("Response from GET request on {}{}", EVENTS_PRIVATE_URI, EVENT_ID_URI);
        return eventService.getEvent(userId, eventId);
    }

    @GetMapping
    public List<ShortEventDTO> getAllEvents(@PathVariable @Positive Long userId,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                            @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Response from GET request on {}", EVENTS_PRIVATE_URI);
        return eventService.getAllEvents(userId, fromSizePage(from, size));
    }

    @PatchMapping(EVENT_ID_URI)
    public FullEventDTO updateEvent(@RequestBody @Valid UpdateEventDTO updateEventDTO,
                                    @PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long eventId) {
        log.info("Response from PATCH request on {}{}", EVENTS_PRIVATE_URI, EVENT_ID_URI);
        if (updateEventDTO.getEventDate() != null) checkEventStart(updateEventDTO.getEventDate());
        return eventService.updateEvent(updateEventDTO, userId, eventId);
    }

    @GetMapping(EVENT_ID_REQUESTS_URI)
    public List<RequestDTO> getRequestsByEventId(@PathVariable @Positive Long userId,
                                                 @PathVariable @Positive Long eventId) {
        log.info("Response from GET request on {}{}", EVENTS_PRIVATE_URI, EVENT_ID_REQUESTS_URI);
        return eventService.getRequestsByEventId(userId, eventId);
    }

    @PatchMapping(EVENT_ID_REQUESTS_URI)
    public RequestResultDTO updateRequestsStatus(@RequestBody @Valid RequestUpdateDTO requestUpdateDTO,
                                                 @PathVariable @Positive Long userId,
                                                 @PathVariable @Positive Long eventId) {
        log.info("Response from PATCH request on {}{}", EVENTS_PRIVATE_URI, EVENT_ID_REQUESTS_URI);
        return eventService.updateRequestsStatus(requestUpdateDTO, userId, eventId);
    }
}