package ru.yandex.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.compilation.dto.CompilationDTO;
import ru.yandex.practicum.compilation.service.CompilationService;

import java.util.List;

import static ru.yandex.practicum.util.Constants.*;
import static ru.yandex.practicum.util.Utilities.fromSizePage;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(COMPILATIONS_PUBLIC_URI)
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping(COMPILATION_ID_URI)
    public CompilationDTO getCompilation(@PathVariable Long compId) {
        log.info("Response from POST request on {}/{}", COMPILATIONS_ADMIN_URI, compId);
        return compilationService.getCompilation(compId);
    }

    @GetMapping
    public List<CompilationDTO> getCompilations(@RequestParam(required = false) Boolean pined,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info("Response from POST request on {}", COMPILATIONS_ADMIN_URI);
        return compilationService.getCompilations(pined, fromSizePage(from, size));
    }
}