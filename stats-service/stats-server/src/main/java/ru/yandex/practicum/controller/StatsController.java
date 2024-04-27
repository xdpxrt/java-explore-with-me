package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.EndpointHit;
import ru.yandex.practicum.dto.ViewStats;
import ru.yandex.practicum.error.exceptions.ValidationException;
import ru.yandex.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.yandex.practicum.util.Constants.DATE_FORMAT;
import static ru.yandex.practicum.util.Constants.END_BEFORE_START;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHit addStat(@RequestBody EndpointHit endpointHit) {
        log.info("Response from POST request on /hit");
        return service.addStat(endpointHit);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStats> getStats(
            @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime end,
            @RequestParam(defaultValue = "") String[] uris,
            @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Response from GET request on /stats");
        if (start.isAfter(end)) throw new ValidationException(END_BEFORE_START);
        return service.getStats(start, end, List.of(uris), unique);
    }
}