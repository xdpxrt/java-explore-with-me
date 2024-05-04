package ru.yandex.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.comment.dto.FullCommentDTO;
import ru.yandex.practicum.comment.dto.ShortCommentDTO;
import ru.yandex.practicum.comment.service.CommentService;

import javax.validation.constraints.Positive;
import java.util.List;

import static ru.yandex.practicum.util.Constants.*;
import static ru.yandex.practicum.util.Utilities.fromSizePage;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class PublicCommentController {
    private final CommentService commentService;

    @GetMapping(COMMENTS_PUBLIC_URI + COMMENTS_ID_URI)
    public FullCommentDTO getComment(@PathVariable @Positive Long commId) {
        log.info("Response from GET request on {}", COMMENTS_PUBLIC_URI + COMMENTS_ID_URI);
        return commentService.getComment(commId);
    }

    @GetMapping(EVENTS_PUBLIC_URI + EVENT_ID_URI + COMMENTS_PUBLIC_URI)
    public List<ShortCommentDTO> getCommentsByEventId(@PathVariable @Positive Long eventId,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "10") int size) {
        log.info("Response from GET request on {}", EVENTS_PUBLIC_URI + EVENT_ID_URI + COMMENTS_PUBLIC_URI);
        return commentService.getCommentsByEventId(eventId, fromSizePage(from, size));
    }
}