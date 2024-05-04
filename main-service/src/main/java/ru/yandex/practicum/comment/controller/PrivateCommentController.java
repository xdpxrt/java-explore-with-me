package ru.yandex.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.comment.dto.FullCommentDTO;
import ru.yandex.practicum.comment.dto.NewCommentDTO;
import ru.yandex.practicum.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static ru.yandex.practicum.util.Constants.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(COMMENTS_PRIVATE_URI)
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping(EVENT_ID_URI)
    @ResponseStatus(HttpStatus.CREATED)
    public FullCommentDTO addComment(@RequestBody @Valid NewCommentDTO newCommentDTO,
                                     @PathVariable @Positive Long userId,
                                     @PathVariable @Positive Long eventId) {
        log.info("Response from POST request on {}", COMMENTS_PRIVATE_URI + EVENT_ID_URI);
        return commentService.addComment(newCommentDTO, userId, eventId);
    }

    @PatchMapping(COMMENTS_ID_URI)
    public FullCommentDTO updateComment(@RequestBody @Valid NewCommentDTO newCommentDTO,
                                        @PathVariable @Positive Long userId,
                                        @PathVariable @Positive Long commId) {
        log.info("Response from PATCH request on {}", COMMENTS_PRIVATE_URI + COMMENTS_ID_URI);
        return commentService.updateComment(newCommentDTO, userId, commId);
    }

    @DeleteMapping(COMMENTS_ID_URI)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @Positive Long userId,
                              @PathVariable @Positive Long commId) {
        log.info("Response from DELETE request on {}", COMMENTS_PRIVATE_URI + COMMENTS_ID_URI);
        commentService.deleteComment(userId, commId);
    }
}
