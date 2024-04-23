package ru.yandex.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.StatsClient;
import ru.yandex.practicum.category.model.Category;
import ru.yandex.practicum.category.repository.CategoryRepository;
import ru.yandex.practicum.dto.EndpointHit;
import ru.yandex.practicum.dto.ViewStats;
import ru.yandex.practicum.error.exception.ConflictException;
import ru.yandex.practicum.error.exception.NotFoundException;
import ru.yandex.practicum.event.dto.FullEventDTO;
import ru.yandex.practicum.event.dto.NewEventDTO;
import ru.yandex.practicum.event.dto.ShortEventDTO;
import ru.yandex.practicum.event.dto.UpdateEventDTO;
import ru.yandex.practicum.event.location.mapper.LocationMapper;
import ru.yandex.practicum.event.location.model.Location;
import ru.yandex.practicum.event.location.repository.LocationRepository;
import ru.yandex.practicum.event.mapper.EventMapper;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.event.state.EventSort;
import ru.yandex.practicum.event.state.EventState;
import ru.yandex.practicum.event.state.StateAction;
import ru.yandex.practicum.request.dto.RequestDTO;
import ru.yandex.practicum.request.dto.RequestResultDTO;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.yandex.practicum.util.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;

    private final StatsClient statsClient;

    @Override
    @Transactional
    public FullEventDTO addEvent(NewEventDTO newEventDTO, Long userId) {
        log.info("Adding event {}", newEventDTO);
        User owner = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format(USER_NOT_FOUND, userId)));
        Category category = categoryRepository.findById(newEventDTO.getCategory()).orElseThrow(() ->
                new NotFoundException(String.format(CATEGORY_NOT_FOUND, newEventDTO.getCategory())));
        Event newEvent = eventMapper.toEvent(newEventDTO, category);
        newEvent.setInitiator(owner);
        newEvent.setState(EventState.PENDING);
        newEvent.setCreatedOn(LocalDateTime.now());
        Location location = locationRepository.save(locationMapper.toLocation(newEventDTO.getLocation()));
        newEvent.setLocation(location);
        Event event = eventRepository.save(newEvent);
        log.info("Event is added {}", event);
        return eventMapper.toFullEventDTO(event);
    }

    @Override
    @Transactional(readOnly = true)
    public FullEventDTO getEvent(Long userId, Long eventId) {
        log.info("Getting event ID{} by user ID{}", eventId, userId);
        return null;
    }

    @Override
    public List<FullEventDTO> getAllEvents(Long userId, PageRequest pageRequest) {
        return null;
    }

    @Override
    public FullEventDTO updateEvent(UpdateEventDTO updateEventDTO, Long userId, Long eventId) {
        log.info("Updating event ID{}", eventId);
        userExistence(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format(EVENT_NOT_FOUND, eventId)));
        if (event.getState() != EventState.PENDING && event.getState() != EventState.CANCELED) {
            throw new ConflictException("Published events can't be changed");
        }
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Event can be changed only by owner");
        }
        if (updateEventDTO.getAnnotation() != null && !updateEventDTO.getAnnotation().equals(event.getAnnotation())) {
            event.setAnnotation(updateEventDTO.getAnnotation());
        }
        if (updateEventDTO.getCategory() != null && !updateEventDTO.getCategory().equals(event.getCategory().getId())) {
            event.setCategory(categoryRepository.findById(updateEventDTO.getCategory()).orElseThrow(() ->
                    new NotFoundException(String.format(CATEGORY_NOT_FOUND, updateEventDTO.getCategory()))));
        }
        if (updateEventDTO.getDescription() != null && !updateEventDTO.getDescription().equals(event.getDescription())) {
            event.setDescription(updateEventDTO.getDescription());
        }
        if (updateEventDTO.getEventDate() != null && !updateEventDTO.getEventDate().equals(event.getEventDate())) {
            event.setEventDate(updateEventDTO.getEventDate());
        }
        if (updateEventDTO.getLocation() != null) {
            event.setLocation(locationRepository.save(locationMapper.toLocation(updateEventDTO.getLocation())));
        }
        if (updateEventDTO.getPaid() != null && !updateEventDTO.getPaid().equals(event.getPaid())) {
            event.setPaid(updateEventDTO.getPaid());
        }
        if (updateEventDTO.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventDTO.getParticipantLimit());
        }
        if (updateEventDTO.getRequestModeration() != null) {
            event.setRequestModeration(updateEventDTO.getRequestModeration());
        }
        if (updateEventDTO.getTitle() != null && !updateEventDTO.getTitle().equals(event.getTitle())) {
            event.setTitle(updateEventDTO.getTitle());
        }
        StateAction stateAction = updateEventDTO.getStateAction();
        if (stateAction != null) {
            switch (stateAction) {
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
            }
        }
        return eventMapper.toFullEventDTO(event);
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
    public List<FullEventDTO> getEventsByAdmin(List<Long> usersIds, List<EventState> statesIds,
                                               List<Long> categoriesIds, LocalDateTime start, LocalDateTime end,
                                               PageRequest pageRequest) {
        return null;
    }

    @Override
    public FullEventDTO updateEventByAdmin(UpdateEventDTO updateEventDTO, Long eventId) {
        return null;
    }

    @Override
    public List<ShortEventDTO> getPublishedEvents(String text, List<Long> categories, Boolean paid,
                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                  Boolean onlyAvailable, EventSort sort, PageRequest pageRequest,
                                                  HttpServletRequest request) {
        return null;
    }

    @Override
    public FullEventDTO getPublishedEventById(Long id, HttpServletRequest request) {
        log.info("Getting published event ID{}", id);
        Event event = eventRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format(EVENT_NOT_FOUND, id)));
        if (!event.getState().equals(EventState.PUBLISHED))
            throw new NotFoundException(String.format("Event ID%d is not public yet", id));
        addStats(request);
        Map<Long, Long> statsMap = getStatsMap(List.of(event));
        Long views = statsMap.getOrDefault(event.getId(), 0L);
        FullEventDTO fullEventDTO = eventMapper.toFullEventDTO(event);
        fullEventDTO.setViews(views);
        return fullEventDTO;
    }

    private void addStats(HttpServletRequest request) {
        statsClient.addStat(EndpointHit.builder()
                .app("explore-with-me")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }

    private Map<Long, Long> getStatsMap(List<Event> events) {
        String eventsUri = EVENTS_PUBLIC_URI + "/";
        List<String> uris = events.stream()
                .map(event -> String.format(eventsUri + "%d", event.getId()))
                .collect(Collectors.toList());
        List<LocalDateTime> startDates = events.stream()
                .map(Event::getCreatedOn)
                .collect(Collectors.toList());
        LocalDateTime earliestDate = startDates.stream()
                .min(LocalDateTime::compareTo)
                .orElse(null);
        if (earliestDate == null) return new HashMap<>();
        List<ViewStats> viewStatsList = statsClient.getStats(earliestDate, LocalDateTime.now(),
                uris, true);
        return viewStatsList.stream()
                .filter(statsDto -> statsDto.getUri().startsWith(eventsUri))
                .collect(Collectors.toMap(
                        statsDto -> Long.parseLong(statsDto.getUri().substring(eventsUri.length())),
                        ViewStats::getHits
                ));
    }

    private void userExistence(Long userId) {
        if (!userRepository.existsById(userId)) throw new NotFoundException(String.format(USER_NOT_FOUND, userId));
    }

    private void categoryExistence(Long categoryId) {
        if (!categoryRepository.existsById(categoryId))
            throw new NotFoundException(String.format(CATEGORY_NOT_FOUND, categoryId));
    }
}