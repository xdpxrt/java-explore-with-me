package ru.yandex.practicum.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.compilation.dto.CompilationDTO;
import ru.yandex.practicum.compilation.dto.NewCompilationDTO;
import ru.yandex.practicum.compilation.model.Compilation;
import ru.yandex.practicum.event.mapper.EventMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = EventMapper.class)
public interface CompilationMapper {
    @Mapping(target = "events", ignore = true)
    Compilation toCompilation(NewCompilationDTO newCompilationDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", expression = "java(eventMapper.toShortEventDTO(compilation.getEvents()))")
    CompilationDTO toCompilationDTO(Compilation compilation, EventMapper eventMapper);

    List<CompilationDTO> toCompilationDTO(List<Compilation> compilations);
}