package ru.yandex.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.compilation.dto.CompilationDTO;
import ru.yandex.practicum.compilation.dto.NewCompilationDTO;
import ru.yandex.practicum.compilation.dto.UpdateCompilationDTO;
import ru.yandex.practicum.compilation.service.CompilationService;

import javax.validation.Valid;

import static ru.yandex.practicum.util.Constants.COMPILATIONS_ADMIN_URI;
import static ru.yandex.practicum.util.Constants.COMPILATION_ID_URI;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(COMPILATIONS_ADMIN_URI)
public class CompilationAdminController {
    public final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CompilationDTO addCompilation(@RequestBody @Valid NewCompilationDTO newCompilationDTO) {
        log.info("Response from POST request on {}", COMPILATIONS_ADMIN_URI);
        if (newCompilationDTO.getPinned() == null) newCompilationDTO.setPinned(false);
        return compilationService.addCompilation(newCompilationDTO);
    }

    @DeleteMapping(COMPILATION_ID_URI)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCompilation(@PathVariable Long compId) {
        log.info("Response from DELETE request on {}/{}", COMPILATIONS_ADMIN_URI, compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping(COMPILATION_ID_URI)
    CompilationDTO updateCompilation(@RequestBody @Valid UpdateCompilationDTO updateCompilationDTO,
                                     @PathVariable Long compId) {
        log.info("Response from PATCH request on {}/{}", COMPILATIONS_ADMIN_URI, compId);
        return compilationService.updateCompilation(updateCompilationDTO, compId);
    }
}