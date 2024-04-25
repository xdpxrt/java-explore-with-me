package ru.yandex.practicum.event.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.category.model.Category;
import ru.yandex.practicum.event.dto.FullEventDTO;
import ru.yandex.practicum.event.dto.NewEventDTO;
import ru.yandex.practicum.event.dto.ShortEventDTO;
import ru.yandex.practicum.event.model.Event;

import java.util.List;

@DecoratedWith(EventMapperDecorator.class)
@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "category", target = "category")
    Event toEvent(NewEventDTO newEventDTO, Category category);

    FullEventDTO toFullEventDTO(Event event);

    ShortEventDTO toShortEventDTO(Event event);

    List<ShortEventDTO> toShortEventDTO(List<Event> event);
}