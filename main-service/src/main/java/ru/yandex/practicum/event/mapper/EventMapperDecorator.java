package ru.yandex.practicum.event.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.category.model.Category;
import ru.yandex.practicum.event.dto.NewEventDTO;
import ru.yandex.practicum.event.model.Event;

public abstract class EventMapperDecorator implements EventMapper {
    @Autowired
    private EventMapper eventMapper;

    @Override
    public Event toEvent(NewEventDTO newEventDTO, Category category) {
        Event event = eventMapper.toEvent(newEventDTO, category);
        event.setCategory(category);
        return event;
    }
}