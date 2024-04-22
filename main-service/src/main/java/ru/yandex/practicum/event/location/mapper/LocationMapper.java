package ru.yandex.practicum.event.location.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.event.location.dto.LocationDTO;
import ru.yandex.practicum.event.location.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationDTO toLocationDTO(Location location);

    Location toLocation(LocationDTO locationDTO);
}