package ru.yandex.practicum.event.location.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.event.location.dto.LocationDTO;
import ru.yandex.practicum.event.location.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(target = "id", ignore = true)
    Location toLocation(LocationDTO locationDTO);
}