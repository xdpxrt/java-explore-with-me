package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.EndpointHit;
import ru.yandex.practicum.model.Stat;

@Mapper(componentModel = "spring")
public interface StatMapper {
    Stat toStat(EndpointHit endpointHit);

    EndpointHit toEndpointHit(Stat stat);
}