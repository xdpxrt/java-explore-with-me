package ru.yandex.practicum.event.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
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

@Service
public class EventServiceImpl implements EventService {
    @Override
    public FullEventDTO addEvent(NewEventDTO newEventDTO, Long userId) {
        return null;
    }

    @Override
    public FullEventDTO getEvent(Long userId, Long eventId) {
        return null;
    }

    @Override
    public List<FullEventDTO> getAllEvents(Long userId, PageRequest pageRequest) {
        return null;
    }

    @Override
    public FullEventDTO updateEvent(UpdateEventDTO updateEventDTO, Long userId, Long eventId) {
        return null;
    }

    @Override
    public List<RequestDTO> getRequestsByEventId(Long userId, Long eventId) {
        return null;
    }

    @Override
    public RequestResultDTO updateRequestsStatus(RequestResultDTO requestResultDTO, Long userId, Long eventId) {
        return null;
    }

    @Override
    public List<FullEventDTO> getEventsByAdmin(List<Long> usersIds, List<EventState> statesIds, List<Long> categoriesIds, LocalDateTime start, LocalDateTime end, PageRequest pageRequest) {
        return null;
    }

    @Override
    public FullEventDTO updateEventByAdmin(UpdateEventDTO updateEventDTO, Long eventId) {
        return null;
    }

    @Override
    public List<ShortEventDTO> getPublishedEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort, PageRequest pageRequest) {
        return null;
    }

    @Override
    public FullEventDTO getPublishedEventById(Long id) {
        return null;
    }
}
