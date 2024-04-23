package ru.yandex.practicum.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.request.dto.RequestDTO;
import ru.yandex.practicum.request.model.Request;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    Request toRequest(RequestDTO requestDTO);

    @Mapping(target = "id", ignore = true)
    RequestDTO toRequestDTO(Request request);
}