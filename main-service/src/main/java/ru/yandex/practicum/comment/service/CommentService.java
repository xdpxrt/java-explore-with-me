package ru.yandex.practicum.comment.service;

import org.springframework.data.domain.PageRequest;
import ru.yandex.practicum.comment.dto.FullCommentDTO;
import ru.yandex.practicum.comment.dto.NewCommentDTO;
import ru.yandex.practicum.comment.dto.ShortCommentDTO;

import java.util.List;

public interface CommentService {
    FullCommentDTO addComment(NewCommentDTO newCommentDTO, Long userId, Long eventId);

    FullCommentDTO updateComment(NewCommentDTO newCommentDTO, Long userId, Long commId);

    void deleteComment(Long userId, Long commId);

    List<FullCommentDTO> getCommentsByAuthorId(Long userId, PageRequest pageRequest);

    void deleteCommentByAdmin(Long commId);

    FullCommentDTO getComment(Long commId);

    List<ShortCommentDTO> getCommentsByEventId(Long eventId, PageRequest pageRequest);
}