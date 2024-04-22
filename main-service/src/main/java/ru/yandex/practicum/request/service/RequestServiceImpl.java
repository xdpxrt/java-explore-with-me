package ru.yandex.practicum.request.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.request.dto.RequestDTO;

import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    @Override
    public RequestDTO addRequest(Long userId, Long eventId) {
        return null;
    }

    @Override
    public List<RequestDTO> getAllRequests(Long userId) {
        return null;
    }

    @Override
    public RequestDTO cancelRequest(Long userId, Long requestId) {
        return null;
    }
}