package ru.yandex.practicum.compilation.service;

import org.springframework.data.domain.PageRequest;
import ru.yandex.practicum.compilation.dto.CompilationDTO;
import ru.yandex.practicum.compilation.dto.NewCompilationDTO;
import ru.yandex.practicum.compilation.dto.UpdateCompilationDTO;

import java.util.List;

public interface CompilationService {
    CompilationDTO addCompilation(NewCompilationDTO newCompilationDTO);

    void deleteCompilation(Long compId);

    CompilationDTO updateCompilation(UpdateCompilationDTO updateCompilationDTO, Long compId);

    CompilationDTO getCompilation(Long compId);

    List<CompilationDTO> getCompilations(Boolean pined, PageRequest pageRequest);
}