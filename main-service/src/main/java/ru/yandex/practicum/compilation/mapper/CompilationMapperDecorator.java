package ru.yandex.practicum.compilation.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.compilation.dto.CompilationDTO;
import ru.yandex.practicum.compilation.model.Compilation;
import ru.yandex.practicum.event.mapper.EventMapper;

public abstract class CompilationMapperDecorator implements CompilationMapper {
    @Autowired
    private CompilationMapper compilationMapper;
    @Autowired
    private EventMapper eventMapper;

    public CompilationDTO compilationDTO(Compilation compilation) {
        CompilationDTO compilationDTO = compilationMapper.toCompilationDTO(compilation);
        compilationDTO.setEvents(eventMapper.toShortEventDTO(compilation.getEvents()));
        return compilationDTO;
    }
}
