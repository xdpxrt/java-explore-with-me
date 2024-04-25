package ru.yandex.practicum.request.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.request.dto.RequestDTO;
import ru.yandex.practicum.request.model.Request;
import ru.yandex.practicum.user.model.User;

import java.util.List;

@DecoratedWith(RequestMapperDecorator.class)
@Mapper(componentModel = "spring")
public interface RequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "event", source = "event")
    @Mapping(target = "requester", source = "requester")
    Request toRequest(RequestDTO requestDTO, Event event, User requester);

    @Mapping(target = "event", expression = "java(request.getEvent().getId())")
    @Mapping(target = "requester", expression = "java(request.getRequester().getId())")
    RequestDTO toRequestDTO(Request request);

    List<RequestDTO> toRequestDTO(List<Request> requests);
}