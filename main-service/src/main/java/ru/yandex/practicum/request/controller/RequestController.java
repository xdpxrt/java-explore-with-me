package ru.yandex.practicum.request.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.request.dto.RequestDTO;
import ru.yandex.practicum.request.service.RequestService;

import javax.validation.constraints.Positive;

import java.util.List;

import static ru.yandex.practicum.util.Constants.REQUESTS_PRIVATE_URI;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(REQUESTS_PRIVATE_URI)
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDTO addRequest(@PathVariable @Positive Long userId,
                                 @RequestParam @Positive Long eventId) {
        log.info("Response from POST request on {}", REQUESTS_PRIVATE_URI);
        return requestService.addRequest(userId, eventId);
    }

    @GetMapping
    public List<RequestDTO> getAllRequests(@PathVariable @Positive Long userId) {
        log.info("Response from GET request on {}", REQUESTS_PRIVATE_URI);
        return requestService.getAllRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDTO cancelRequest(@PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long requestId) {
        log.info("Response from GET request on {}", REQUESTS_PRIVATE_URI);
        return requestService.cancelRequest(userId, requestId);
    }
}