package ru.yandex.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.compilation.dto.CompilationDTO;
import ru.yandex.practicum.compilation.dto.NewCompilationDTO;
import ru.yandex.practicum.compilation.dto.UpdateCompilationDTO;
import ru.yandex.practicum.compilation.mapper.CompilationMapper;
import ru.yandex.practicum.compilation.model.Compilation;
import ru.yandex.practicum.compilation.repository.CompilationRepository;
import ru.yandex.practicum.error.exception.NotFoundException;
import ru.yandex.practicum.event.mapper.EventMapper;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.repository.EventRepository;

import java.util.HashSet;
import java.util.List;

import static ru.yandex.practicum.util.Constants.COMPILATION_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationsService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;

    @Override
    @Transactional
    public CompilationDTO addCompilation(NewCompilationDTO newCompilationDTO) {
        log.info("Adding compilation {}", newCompilationDTO);
        Compilation compilation = compilationMapper.toCompilation(newCompilationDTO);
        HashSet<Event> events = newCompilationDTO.getEvents() == null ? new HashSet<>()
                : new HashSet<>(eventRepository.findAllByIdIn(newCompilationDTO.getEvents()));
        compilation.setEvents(events);
        return compilationMapper.toCompilationDTO(compilationRepository.save(compilation), eventMapper);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        log.info("Deleting compilation ID{}", compId);
        getCompilationIfExist(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDTO updateCompilation(UpdateCompilationDTO updateCompilationDTO, Long compId) {
        log.info("Updating compilation ID{}", compId);
        Compilation compilation = getCompilationIfExist(compId);
        if (updateCompilationDTO.getEvents() != null) {
            HashSet<Event> events = new HashSet<>(eventRepository.findAllByIdIn(updateCompilationDTO.getEvents()));
            compilation.setEvents(events);
        }
        if (updateCompilationDTO.getPinned() != null) compilation.setPinned(updateCompilationDTO.getPinned());
        if (updateCompilationDTO.getTitle() != null) compilation.setTitle(updateCompilationDTO.getTitle());
        return compilationMapper.toCompilationDTO(compilationRepository.save(compilation), eventMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDTO getCompilation(Long compId) {
        log.info("Getting compilation ID{}", compId);
        return compilationMapper.toCompilationDTO(getCompilationIfExist(compId), eventMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDTO> getCompilations(Boolean pined, PageRequest pageRequest) {
        log.info("Getting compilations");
        return (pined == null) ? compilationMapper.toCompilationDTO(compilationRepository.findAll(pageRequest).toList())
                : compilationMapper.toCompilationDTO(compilationRepository.findAllByPinned(pined, pageRequest));
    }

    @Transactional(readOnly = true)
    private Compilation getCompilationIfExist(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format(COMPILATION_NOT_FOUND, compId)));
    }
}