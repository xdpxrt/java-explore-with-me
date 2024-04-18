package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.EndpointHit;
import ru.yandex.practicum.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    EndpointHit addStat(EndpointHit hit);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}