package ru.yandex.practicum.request.service;

import ru.yandex.practicum.request.dto.RequestDTO;

import java.util.List;

public interface RequestService {
    RequestDTO addRequest(Long userId, Long eventId);

    List<RequestDTO> getAllRequests(Long userId);

    RequestDTO cancelRequest(Long userId, Long requestId);
}