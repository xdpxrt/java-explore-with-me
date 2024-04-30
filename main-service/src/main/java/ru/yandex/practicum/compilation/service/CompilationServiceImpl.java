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
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.event.service.EventService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static ru.yandex.practicum.util.Constants.COMPILATION_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    private final CompilationMapper compilationMapper;
    private final EventService eventService;

    @Override
    @Transactional
    public CompilationDTO addCompilation(NewCompilationDTO newCompilationDTO) {
        log.debug("Adding compilation {}", newCompilationDTO);
        Compilation compilation = compilationMapper.toCompilation(newCompilationDTO);
        List<Event> events = newCompilationDTO.getEvents() == null ? new ArrayList<>()
                : eventRepository.findAllByIdIn(newCompilationDTO.getEvents());
        events = eventService.fillWithConfirmedRequests(events);
        events = eventService.fillWithEventViews(events);
        compilation.setEvents(new HashSet<>(events));
        CompilationDTO compilationDTO = compilationMapper.toCompilationDTO(compilationRepository.save(compilation));
        log.debug("Added new compilation {}", compilationDTO);
        return compilationDTO;
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        log.debug("Deleting compilation ID{}", compId);
        getCompilationIfExist(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDTO updateCompilation(UpdateCompilationDTO updateCompilationDTO, Long compId) {
        log.debug("Updating compilation ID{}", compId);
        Compilation compilation = getCompilationIfExist(compId);
        if (updateCompilationDTO.getEvents() != null) {
            HashSet<Event> events = new HashSet<>(eventRepository.findAllByIdIn(updateCompilationDTO.getEvents()));
            compilation.setEvents(events);
        }
        if (updateCompilationDTO.getPinned() != null) compilation.setPinned(updateCompilationDTO.getPinned());
        if (updateCompilationDTO.getTitle() != null) compilation.setTitle(updateCompilationDTO.getTitle());

        return compilationMapper.toCompilationDTO(compilationRepository.save(compilation));
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDTO getCompilation(Long compId) {
        log.debug("Getting compilation ID{}", compId);
        return compilationMapper.toCompilationDTO(getCompilationIfExist(compId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDTO> getCompilations(Boolean pined, PageRequest pageRequest) {
        log.debug("Getting compilations");
        return (pined == null) ? compilationMapper.toCompilationDTO(compilationRepository.findAll(pageRequest).toList())
                : compilationMapper.toCompilationDTO(compilationRepository.findAllByPinned(pined, pageRequest).toList());

    }

    private Compilation getCompilationIfExist(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format(COMPILATION_NOT_FOUND, compId)));
    }
}