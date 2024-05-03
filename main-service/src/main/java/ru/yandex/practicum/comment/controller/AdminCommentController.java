package ru.yandex.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.comment.dto.FullCommentDTO;
import ru.yandex.practicum.comment.service.CommentService;

import javax.validation.constraints.Positive;
import java.util.List;

import static ru.yandex.practicum.util.Constants.*;
import static ru.yandex.practicum.util.Utilities.fromSizePage;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(COMMENTS_ADMIN_URI)
public class AdminCommentController {
    private final CommentService commentService;

    @GetMapping(USER_ID_URI)
    public List<FullCommentDTO> getCommentsByAuthorId(@PathVariable @Positive Long userId,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        log.info("Response from GET request on {}", COMMENTS_ADMIN_URI + USER_ID_URI);
        return commentService.getCommentsByAuthorId(userId, fromSizePage(from, size));
    }

    @DeleteMapping(COMMENTS_ID_URI)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @Positive Long commId) {
        log.info("Response from DELETE request on {}", COMMENTS_ADMIN_URI + USER_ID_URI);
        commentService.deleteCommentByAdmin(commId);
    }
}