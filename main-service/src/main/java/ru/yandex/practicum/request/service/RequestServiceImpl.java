package ru.yandex.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.error.exception.ConflictException;
import ru.yandex.practicum.error.exception.NotFoundException;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.request.dto.RequestDTO;
import ru.yandex.practicum.request.mapper.RequestMapper;
import ru.yandex.practicum.request.model.Request;
import ru.yandex.practicum.request.repository.RequestRepository;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.user.repository.UserRepository;
import ru.yandex.practicum.util.Constants;

import java.time.LocalDateTime;
import java.util.List;

import static ru.yandex.practicum.util.Constants.EVENT_NOT_FOUND;
import static ru.yandex.practicum.util.Constants.RequestStatus.*;
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
    @Transactional
    public RequestDTO addRequest(Long userId, Long eventId) {
        log.debug("Adding request from user ID{} to event ID{}", userId, eventId);
        User user = getUserIfExist(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format(EVENT_NOT_FOUND, eventId)));
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("The event manager is unable to make a request for his event");
        }
        if (!event.getState().equals(Constants.EventState.PUBLISHED)) {
            throw new ConflictException(String.format("Event ID%d still not published", eventId));
        }
        if (requestRepository.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new ConflictException(String.format("User ID%d already send request on event ID%d", userId, eventId));
        }
        List<Request> list = requestRepository.findAll();
        Long confirmedRequests = requestRepository.countByEventIdAndStatus(eventId, CONFIRMED);
        if (event.getParticipantLimit() > 0 && event.getParticipantLimit() <= confirmedRequests) {
            throw new ConflictException("Limit of the participants is already reached");
        }
        Constants.RequestStatus status;
        status = event.getRequestModeration() ? PENDING : CONFIRMED;
        if (event.getParticipantLimit() == 0) status = CONFIRMED;
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(status)
                .build();
        return requestMapper.toRequestDTO(requestRepository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDTO> getAllRequests(Long userId) {
        log.debug(String.format("Getting requests of user ID%d", userId));
        getUserIfExist(userId);
        return requestMapper.toRequestDTO(requestRepository.findAllByRequesterId(userId));
    }

    @Override
    public RequestDTO cancelRequest(Long userId, Long requestId) {
        log.debug(String.format("Canceling request ID%d", requestId));
        getUserIfExist(userId);
        Request request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException(String.format("Request ID%d doesn't exist", requestId)));
        request.setStatus(CANCELED);
        return requestMapper.toRequestDTO(requestRepository.save(request));
    }

    private User getUserIfExist(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format(USER_NOT_FOUND, userId)));
    }
}