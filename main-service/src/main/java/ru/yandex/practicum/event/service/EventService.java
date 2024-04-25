package ru.yandex.practicum.event.service;

import org.springframework.data.domain.PageRequest;
import ru.yandex.practicum.event.dto.*;
import ru.yandex.practicum.event.state.EventSort;
import ru.yandex.practicum.event.state.EventState;
import ru.yandex.practicum.request.dto.RequestDTO;
import ru.yandex.practicum.request.dto.RequestResultDTO;
import ru.yandex.practicum.request.dto.RequestUpdateDTO;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    FullEventDTO addEvent(NewEventDTO newEventDTO, Long userId);

    FullEventDTO getEvent(Long userId, Long eventId);

    List<ShortEventDTO> getAllEvents(Long userId, PageRequest pageRequest);

    FullEventDTO updateEvent(UserUpdateEventDTO userUpdateEventDTO, Long userId, Long eventId);

    List<RequestDTO> getRequestsByEventId(Long userId, Long eventId);

    RequestResultDTO updateRequestsStatus(RequestUpdateDTO requestUpdateDTO, Long userId, Long eventId);

    List<FullEventDTO> getEventsByAdmin(List<Long> usersIds, List<EventState> statesIds, List<Long> categoriesIds,
                                        LocalDateTime start, LocalDateTime end, PageRequest pageRequest);

    FullEventDTO updateEventByAdmin(AdminUpdateEventDTO userUpdateEventDTO, Long eventId);

    List<ShortEventDTO> getPublishedEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort,
                                           PageRequest pageRequest, HttpServletRequest request);

    FullEventDTO getPublishedEventById(Long id, HttpServletRequest request);
}