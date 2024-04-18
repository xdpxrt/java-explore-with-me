package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.yandex.practicum.dto.EndpointHit;
import ru.yandex.practicum.dto.ViewStats;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsClient {

    private final WebClient webClient;
    private final ResourceBundle resource = ResourceBundle.getBundle("messages");

    public EndpointHit addStat(EndpointHit endpointHit) {
        String uri = resource.getString("client.hits");
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
        String uri = resource.getString("client.stats");
        String request = String.format(uri, encode(String.valueOf(start)), encode(String.valueOf(end)), uris, unique);
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