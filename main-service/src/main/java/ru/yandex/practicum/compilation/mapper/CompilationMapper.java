package ru.yandex.practicum.compilation.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.compilation.dto.CompilationDTO;
import ru.yandex.practicum.compilation.dto.NewCompilationDTO;
import ru.yandex.practicum.compilation.model.Compilation;
import ru.yandex.practicum.event.mapper.EventMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = EventMapper.class)
@DecoratedWith(CompilationMapperDecorator.class)
public interface CompilationMapper {
    @Mapping(target = "events", ignore = true)
    Compilation toCompilation(NewCompilationDTO newCompilationDTO);

    @Mapping(target = "id", ignore = true)
    CompilationDTO toCompilationDTO(Compilation compilation);

    List<CompilationDTO> toCompilationDTO(List<Compilation> compilations);
}