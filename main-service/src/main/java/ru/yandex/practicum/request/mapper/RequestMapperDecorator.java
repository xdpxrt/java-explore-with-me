package ru.yandex.practicum.request.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.request.dto.RequestDTO;
import ru.yandex.practicum.request.model.Request;
import ru.yandex.practicum.user.model.User;

public abstract class RequestMapperDecorator implements RequestMapper {
    @Autowired
    private RequestMapper requestMapper;

    @Override
    public Request toRequest(RequestDTO requestDTO, Event event, User requester) {
        Request request = requestMapper.toRequest(requestDTO, event, requester);
        request.setEvent(event);
        request.setRequester(requester);
        return request;
    }
}