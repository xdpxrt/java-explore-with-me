package ru.yandex.practicum.comment.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.comment.dto.FullCommentDTO;
import ru.yandex.practicum.comment.dto.ShortCommentDTO;
import ru.yandex.practicum.comment.model.Comment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    FullCommentDTO toFullCommentDTO(Comment comment);

    List<FullCommentDTO> toFullCommentDTO(List<Comment> comment);

    ShortCommentDTO toShortCommentDTO(Comment comment);

    List<ShortCommentDTO> toShortCommentDTO(List<Comment> comment);
}