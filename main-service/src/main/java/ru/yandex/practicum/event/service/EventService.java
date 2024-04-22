package ru.yandex.practicum.event.service;

import org.springframework.data.domain.PageRequest;
import ru.yandex.practicum.event.dto.FullEventDTO;
import ru.yandex.practicum.event.dto.NewEventDTO;
import ru.yandex.practicum.event.dto.ShortEventDTO;
import ru.yandex.practicum.event.dto.UpdateEventDTO;
import ru.yandex.practicum.event.state.EventSort;
import ru.yandex.practicum.event.state.EventState;
import ru.yandex.practicum.request.dto.RequestDTO;
import ru.yandex.practicum.request.dto.RequestResultDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    FullEventDTO addEvent(NewEventDTO newEventDTO, Long userId);

    FullEventDTO getEvent(Long userId, Long eventId);

    List<FullEventDTO> getAllEvents(Long userId, PageRequest pageRequest);

    FullEventDTO updateEvent(UpdateEventDTO updateEventDTO, Long userId, Long eventId);

    List<RequestDTO> getRequestsByEventId(Long userId, Long eventId);

    RequestResultDTO updateRequestsStatus(RequestResultDTO requestResultDTO, Long userId, Long eventId);

    List<FullEventDTO> getEventsByAdmin(List<Long> usersIds, List<EventState> statesIds, List<Long> categoriesIds,
                                        LocalDateTime start, LocalDateTime end, PageRequest pageRequest);

    FullEventDTO updateEventByAdmin(UpdateEventDTO updateEventDTO, Long eventId);

    List<ShortEventDTO> getPublishedEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort,
                                           PageRequest pageRequest);

    FullEventDTO getPublishedEventById(Long id);
}