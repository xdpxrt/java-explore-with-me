package ru.yandex.practicum.event.service;

import org.springframework.data.domain.PageRequest;
import ru.yandex.practicum.event.dto.FullEventDTO;
import ru.yandex.practicum.event.dto.NewEventDTO;
import ru.yandex.practicum.event.dto.ShortEventDTO;
import ru.yandex.practicum.event.dto.UpdateEventDTO;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.request.dto.RequestDTO;
import ru.yandex.practicum.request.dto.RequestResultDTO;
import ru.yandex.practicum.request.dto.RequestUpdateDTO;
import ru.yandex.practicum.util.Constants;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    FullEventDTO addEvent(NewEventDTO newEventDTO, Long userId);

    FullEventDTO getEvent(Long userId, Long eventId);

    List<ShortEventDTO> getAllEvents(Long userId, PageRequest pageRequest);

    FullEventDTO updateEvent(UpdateEventDTO updateEventDTO, Long userId, Long eventId);

    List<RequestDTO> getRequestsByEventId(Long userId, Long eventId);

    RequestResultDTO updateRequestsStatus(RequestUpdateDTO requestUpdateDTO, Long userId, Long eventId);

    List<FullEventDTO> getEventsByAdmin(List<Long> usersIds, List<Constants.EventState> states, List<Long> categoriesIds,
                                        LocalDateTime start, LocalDateTime end, PageRequest pageRequest);

    FullEventDTO updateEventByAdmin(UpdateEventDTO userUpdateEventDTO, Long eventId);

    List<ShortEventDTO> getPublishedEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, Boolean onlyAvailable, Constants.EventSort sort,
                                           PageRequest pageRequest, HttpServletRequest request);

    FullEventDTO getPublishedEventById(Long id, HttpServletRequest request);

    public List<Event> fillWithEventViews(List<Event> events);

    List<Event> fillWithConfirmedRequests(List<Event> events);

}