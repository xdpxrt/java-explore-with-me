package ru.yandex.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.error.exception.ConflictException;
import ru.yandex.practicum.error.exception.NotFoundException;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.event.state.EventState;
import ru.yandex.practicum.request.dto.RequestDTO;
import ru.yandex.practicum.request.mapper.RequestMapper;
import ru.yandex.practicum.request.model.Request;
import ru.yandex.practicum.request.repository.RequestRepository;
import ru.yandex.practicum.request.status.RequestStatus;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.yandex.practicum.request.status.RequestStatus.CONFIRMED;
import static ru.yandex.practicum.request.status.RequestStatus.PENDING;
import static ru.yandex.practicum.util.Constants.EVENT_NOT_FOUND;
import static ru.yandex.practicum.util.Constants.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    private final RequestMapper requestMapper;

    @Override
    public RequestDTO addRequest(Long userId, Long eventId) {
        log.info("Adding request from user ID{} to event ID{}", userId, eventId);
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format(USER_NOT_FOUND, userId)));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format(EVENT_NOT_FOUND, eventId)));
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("The event manager is unable to make a request for his event");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException(String.format("Event ID%d still not published", eventId));
        }
        if (requestRepository.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new ConflictException(String.format("User ID%d already send request on event ID%d", userId, eventId));
        }
        Long confirmedRequests = requestRepository.countByEventIdAndStatus(eventId, CONFIRMED);
        if (event.getParticipantLimit() > 0 && event.getParticipantLimit() <= confirmedRequests) {
            throw new ConflictException("Limit of the participants is already reached");
        }
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status((event.getRequestModeration()) ? PENDING : CONFIRMED)
                .build();
        return requestMapper.toRequestDTO(requestRepository.save(request));
    }

    @Override
    public List<RequestDTO> getAllRequests(Long userId) {
        return null;
    }

    @Override
    public RequestDTO cancelRequest(Long userId, Long requestId) {
        return null;
    }

    private void userExistence(Long userId) {
        if (!userRepository.existsById(userId)) throw new NotFoundException(String.format(USER_NOT_FOUND, userId));
    }

    private void eventExistence(Long eventId) {
        if (!eventRepository.existsById(eventId)) throw new NotFoundException(String.format(EVENT_NOT_FOUND, eventId));
    }
}