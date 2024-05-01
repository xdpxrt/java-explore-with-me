package ru.yandex.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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
import ru.yandex.practicum.request.dto.RequestDTO;
import ru.yandex.practicum.request.dto.RequestResultDTO;
import ru.yandex.practicum.request.dto.RequestUpdateDTO;
import ru.yandex.practicum.request.mapper.RequestMapper;
import ru.yandex.practicum.request.model.Request;
import ru.yandex.practicum.request.repository.RequestRepository;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.util.Constants.*;
import static ru.yandex.practicum.util.Constants.EventState.*;
import static ru.yandex.practicum.util.Constants.RequestStatus.CONFIRMED;
import static ru.yandex.practicum.util.Constants.RequestStatus.REJECTED;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;

    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;
    private final RequestMapper requestMapper;

    private final StatsClient statsClient;

    @Override
    @Transactional
    public FullEventDTO addEvent(NewEventDTO newEventDTO, Long userId) {
        log.debug("Adding event {}", newEventDTO);
        User owner = getUser(userId);
        Category category = getCategory(newEventDTO.getCategory());
        Event newEvent = eventMapper.toEvent(newEventDTO, category);
        newEvent.setInitiator(owner);
        Location location = locationRepository.save(locationMapper.toLocation(newEventDTO.getLocation()));
        newEvent.setLocation(location);
        newEvent.setCreatedOn(LocalDateTime.now());
        newEvent.setState(PENDING);
        Event event = eventRepository.save(newEvent);
        event.setViews(0L);
        event.setConfirmedRequests(0L);
        log.debug("Event is added {}", event);
        return eventMapper.toFullEventDTO(event);
    }

    @Override
    @Transactional(readOnly = true)
    public FullEventDTO getEvent(Long userId, Long eventId) {
        log.debug("Getting event ID{} by user ID{}", eventId, userId);
        getUser(userId);
        Event event = getEvent(eventId);
        event = fillWithEventViews(List.of(event)).get(0);
        event = fillWithConfirmedRequests(List.of(event)).get(0);
        return eventMapper.toFullEventDTO(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShortEventDTO> getAllEvents(Long userId, PageRequest pageRequest) {
        log.debug("Getting events by user ID{}", userId);
        getUser(userId);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageRequest);
        events = fillWithEventViews(events);
        events = fillWithConfirmedRequests(events);
        return eventMapper.toShortEventDTO(events);
    }

    @Override
    @Transactional
    public FullEventDTO updateEvent(UpdateEventDTO updateEventDTO, Long userId, Long eventId) {
        log.debug("Updating event ID{}", eventId);
        getUser(userId);
        Event event = getEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Event can be changed only by owner");
        }
        Event updatedEvent = checkEventForUpdate(event, updateEventDTO);
        if (updateEventDTO.getStateAction() != null) {
            switch (updateEventDTO.getStateAction()) {
                case CANCEL_REVIEW:
                    updatedEvent.setState(CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    updatedEvent.setState(PENDING);
                    break;
                default:
                    throw new ConflictException("Wrong state action");
            }
        }
        event = eventRepository.save(updatedEvent);
        event.setViews(0L);
        event.setConfirmedRequests(0L);
        return eventMapper.toFullEventDTO(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDTO> getRequestsByEventId(Long userId, Long eventId) {
        log.debug("Getting requests of event ID{}", eventId);
        getUser(userId);
        getEvent(eventId);
        return requestMapper.toRequestDTO(requestRepository.findAllByEventId(eventId));
    }

    @Override
    @Transactional
    public RequestResultDTO updateRequestsStatus(RequestUpdateDTO requestUpdateDTO, Long userId, Long eventId) {
        log.debug("Updating requests {}", requestUpdateDTO.getRequestIds());
        getUser(userId);
        Event event = getEvent(eventId);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0)
            throw new ConflictException("There is no need to update requests, " +
                    "due to the unlimited number of participants or moderation is off");
        Long numOfConfirmedRequests = requestRepository.countByEventIdAndStatus(eventId, CONFIRMED);
        if (event.getParticipantLimit().equals(numOfConfirmedRequests))
            throw new ConflictException("Limit of participant is reached");
        RequestResultDTO requestResultDTO = RequestResultDTO
                .builder()
                .confirmedRequests(new ArrayList<>())
                .rejectedRequests(new ArrayList<>())
                .build();
        List<Request> requestsToUpdate = requestRepository.findAllByIdIn(requestUpdateDTO.getRequestIds());
        for (Request request : requestsToUpdate) {
            if (event.getParticipantLimit().equals(numOfConfirmedRequests)) break;
            RequestDTO requestDTO;
            switch (requestUpdateDTO.getStatus()) {
                case REJECTED:
                    request.setStatus(REJECTED);
                    requestDTO = requestMapper.toRequestDTO(requestRepository.save(request));
                    requestResultDTO.getRejectedRequests().add(requestDTO);
                    break;
                case CONFIRMED:
                    request.setStatus(CONFIRMED);
                    requestDTO = requestMapper.toRequestDTO(requestRepository.save(request));
                    requestResultDTO.getConfirmedRequests().add(requestDTO);
                    numOfConfirmedRequests++;
                    break;
                default:
                    throw new ConflictException("Wrong request status");
            }
        }
        return requestResultDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FullEventDTO> getEventsByAdmin(List<Long> usersIds, List<EventState> states,
                                               List<Long> categoriesIds, LocalDateTime start, LocalDateTime end,
                                               PageRequest pageRequest) {
        log.debug("Getting events by admin");
        List<Specification<Event>> specifications = new ArrayList<>();
        specifications.add(states.isEmpty() ? null : stateIn(states));
        specifications.add(usersIds.isEmpty() ? null : userIdIn(usersIds));
        specifications.add(states.isEmpty() ? null : stateIn(states));
        specifications.add(categoriesIds.isEmpty() ? null : categoryIdIn(categoriesIds));
        specifications.add(start == null ? null : eventDateAfter(start));
        specifications.add(end == null ? null : eventDateBefore(end));
        specifications = specifications.stream().filter(Objects::nonNull).collect(Collectors.toList());
        List<Event> events = eventRepository.findAll(specifications
                .stream()
                .reduce(Specification::and)
                .orElse(null), pageRequest).toList();
        events = fillWithConfirmedRequests(events);
        events = fillWithEventViews(events);
        return events.stream()
                .map(eventMapper::toFullEventDTO)
                .sorted(Comparator.comparing(FullEventDTO::getId, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FullEventDTO updateEventByAdmin(UpdateEventDTO updateEventDTO, Long eventId) {
        log.debug(String.format("Updating event ID%d by admin", eventId));
        Event event = checkEventForUpdate(getEvent(eventId), updateEventDTO);
        if (event.getState().equals(CANCELED)) throw new ConflictException("Not possible to update canceled event");
        if (updateEventDTO.getStateAction() != null) {
            switch (updateEventDTO.getStateAction()) {
                case REJECT_EVENT:
                    event.setState(CANCELED);
                    break;
                case PUBLISH_EVENT:
                    event.setPublishedOn(LocalDateTime.now());
                    event.setState(PUBLISHED);
                    break;
                default:
                    throw new ConflictException("Wrong state action");
            }
        }
        Event updatedEvent = eventRepository.save(event);
        event.setViews(0L);
        event.setConfirmedRequests(0L);
        log.debug(String.format("Event ID%d updated  by admin %s", eventId, updatedEvent));
        return eventMapper.toFullEventDTO(updatedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShortEventDTO> getPublishedEvents(String text, List<Long> categories, Boolean paid,
                                                  LocalDateTime start, LocalDateTime end,
                                                  Boolean onlyAvailable, EventSort sort, PageRequest pageRequest,
                                                  HttpServletRequest request) {
        log.debug("Getting published events by parameters");
        addStats(request);
        List<Specification<Event>> specifications = new ArrayList<>();
        specifications.add(stateIn(List.of(PUBLISHED)));
        specifications.add(text.isBlank() ? null : annotationAndDescriptionContaining(text));
        specifications.add(categories.isEmpty() ? null : categoryIdIn(categories));
        specifications.add(paid == null ? null : paidIs(paid));
        specifications.add(start == null ? null : eventDateAfter(start));
        specifications.add(end == null ? null : eventDateBefore(end));
        specifications = specifications.stream().filter(Objects::nonNull).collect(Collectors.toList());
        Specification<Event> s = specifications
                .stream()
                .reduce(Specification::and).orElse(null);

        List<Event> events = eventRepository.findAll(s, pageRequest).toList();
        events = fillWithConfirmedRequests(events);
        events = onlyAvailable ? events
                .stream()
                .filter(event -> event.getParticipantLimit() > event.getConfirmedRequests())
                .collect(Collectors.toList()) : events;
        events = fillWithEventViews(events);
        if (sort == null) return eventMapper.toShortEventDTO(events);
        switch (sort) {
            case VIEWS:
                return events
                        .stream()
                        .sorted(Comparator.comparing(Event::getViews))
                        .map(eventMapper::toShortEventDTO)
                        .collect(Collectors.toList());
            case EVENT_DATE:
                return events
                        .stream()
                        .sorted(Comparator.comparing(Event::getEventDate))
                        .map(eventMapper::toShortEventDTO)
                        .collect(Collectors.toList());
            default:
                return eventMapper.toShortEventDTO(events);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public FullEventDTO getPublishedEventById(Long id, HttpServletRequest request) {
        log.debug("Getting published event ID{}", id);
        Event event = getEvent(id);
        if (!event.getState().equals(PUBLISHED))
            throw new NotFoundException(String.format("Event ID%d is not published yet", id));
        addStats(request);
        event = fillWithEventViews(List.of(event)).get(0);
        event = fillWithConfirmedRequests(List.of(event)).get(0);
        return eventMapper.toFullEventDTO(event);
    }

    private void addStats(HttpServletRequest request) {
        statsClient.addStat(EndpointHit.builder()
                .app("explore-with-me")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @Override
    public List<Event> fillWithEventViews(List<Event> events) {
        String eventsUri = EVENTS_PUBLIC_URI + "/";
        List<String> uris = events
                .stream()
                .map(event -> String.format(eventsUri + "%d", event.getId()))
                .collect(Collectors.toList());
        List<LocalDateTime> startDates = events
                .stream()
                .map(Event::getCreatedOn)
                .sorted()
                .collect(Collectors.toList());
        if (startDates.stream().findFirst().isEmpty()) return events;
        List<ViewStats> viewStatsList = statsClient.getStats(startDates.stream().findFirst().get(), LocalDateTime.now(),
                uris, true);
        Map<Long, Long> mapEventIdViews = viewStatsList
                .stream()
                .collect(Collectors.toMap(
                        statsDto -> Long.parseLong(statsDto.getUri().substring(eventsUri.length())),
                        ViewStats::getHits));
        return events
                .stream()
                .peek(event -> event.setViews(mapEventIdViews.getOrDefault(event.getId(), 0L)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> fillWithConfirmedRequests(List<Event> events) {
        List<Request> confirmedRequests = requestRepository.findAllByEventIdInAndStatus(events
                .stream()
                .map(Event::getId)
                .collect(Collectors.toList()), CONFIRMED);
        Map<Long, List<Request>> mapEventIdConfirmedRequests = confirmedRequests
                .stream()
                .collect(Collectors.groupingBy(request -> request.getEvent().getId()));
        return events
                .stream()
                .peek(event -> event.setConfirmedRequests((long) mapEventIdConfirmedRequests
                        .getOrDefault(event.getId(), new ArrayList<>()).size()))
                .collect(Collectors.toList());
    }

    private Event checkEventForUpdate(Event event, UpdateEventDTO updateEventDTO) {
        if (event.getState().equals(PUBLISHED))
            throw new ConflictException("Published events cannot be changed");
        if (updateEventDTO.getAnnotation() != null && !updateEventDTO.getAnnotation()
                .equals(event.getAnnotation())) {
            event.setAnnotation(updateEventDTO.getAnnotation());
        }
        if (updateEventDTO.getCategory() != null && !updateEventDTO.getCategory()
                .equals(event.getCategory().getId())) {
            event.setCategory(getCategory(updateEventDTO.getCategory()));
        }
        if (updateEventDTO.getDescription() != null && !updateEventDTO.getDescription()
                .equals(event.getDescription())) {
            event.setDescription(updateEventDTO.getDescription());
        }
        if (updateEventDTO.getEventDate() != null && !updateEventDTO.getEventDate()
                .equals(event.getEventDate())) {
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
        return event;
    }

    private Specification<Event> userIdIn(List<Long> usersIds) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("initiator").get("id")).value(usersIds));
    }

    private Specification<Event> stateIn(List<EventState> states) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("state")).value(states));
    }

    private Specification<Event> categoryIdIn(List<Long> categoriesIds) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("category").get("id"))
                .value(categoriesIds));
    }

    private Specification<Event> eventDateAfter(LocalDateTime rangeStart) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
    }

    private Specification<Event> eventDateBefore(LocalDateTime rangeEnd) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
    }

    private Specification<Event> annotationAndDescriptionContaining(String text) {
        String searchText = "%" + text.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), searchText),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchText)));
    }

    private Specification<Event> paidIs(Boolean paid) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paid"), paid));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format(USER_NOT_FOUND, userId)));
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException(String.format(CATEGORY_NOT_FOUND, categoryId)));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format(EVENT_NOT_FOUND, eventId)));
    }
}