package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsClient {

    private final WebClient webClient;

    public EndpointHit addStat(EndpointHit endpointHit) {
        String uri = "/hits";
        log.info("Request on uri {} body {}", uri, endpointHit);
        return webClient
                .post()
                .uri(uri)
                .bodyValue(endpointHit)
                .retrieve()
                .bodyToMono(EndpointHit.class)
                .block();
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String request = String.format("/stats?start=%s&end=%s&uris=%s&unique=%s",
                encode(String.valueOf(start)), encode(String.valueOf(end)), uris, unique);
        log.info("Request on uri {}", request);
        return webClient
                .get()
                .uri(request)
                .retrieve()
                .bodyToFlux(ViewStats.class)
                .collectList()
                .block();
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}