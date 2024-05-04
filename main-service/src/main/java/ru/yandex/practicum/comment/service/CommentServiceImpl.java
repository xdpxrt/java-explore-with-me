package ru.yandex.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.comment.dto.FullCommentDTO;
import ru.yandex.practicum.comment.dto.NewCommentDTO;
import ru.yandex.practicum.comment.dto.ShortCommentDTO;
import ru.yandex.practicum.comment.mapper.CommentMapper;
import ru.yandex.practicum.comment.model.Comment;
import ru.yandex.practicum.comment.repository.CommentRepository;
import ru.yandex.practicum.error.exception.ConflictException;
import ru.yandex.practicum.error.exception.NotFoundException;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.yandex.practicum.util.Constants.*;
import static ru.yandex.practicum.util.Constants.EventState.PUBLISHED;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public FullCommentDTO addComment(NewCommentDTO newCommentDTO, Long userId, Long eventId) {
        log.debug("Adding comment to event ID{} from user ID{} {}", eventId, userId, newCommentDTO);
        User user = getUserIfExist(userId);
        Event event = getEventIfExist(eventId);
        if (!PUBLISHED.equals(event.getState()))
            throw new ConflictException("Only to published events you can add comment");
        Comment comment = Comment.builder()
                .text(newCommentDTO.getText())
                .author(user)
                .event(event)
                .createdOn(LocalDateTime.now())
                .updatedOn(null)
                .build();
        FullCommentDTO fullCommentDTO = commentMapper.toFullCommentDTO(commentRepository.save(comment));
        log.debug("Comment added {}", fullCommentDTO);
        return fullCommentDTO;
    }

    @Override
    @Transactional
    public FullCommentDTO updateComment(NewCommentDTO newCommentDTO, Long userId, Long commId) {
        log.debug("Updating comment ID{} {}", userId, newCommentDTO);
        getUserIfExist(userId);
        Comment comment = getCommentIfExist(commId);
        if (!userId.equals(comment.getAuthor().getId()))
            throw new ConflictException("Only author can update the comment");
        comment.setText(newCommentDTO.getText());
        comment.setUpdatedOn(LocalDateTime.now());
        FullCommentDTO fullCommentDTO = commentMapper.toFullCommentDTO(commentRepository.save(comment));
        log.debug("Comment updated {}", fullCommentDTO);
        return fullCommentDTO;
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long commId) {
        log.debug("Deleting comment ID{}", commId);
        getUserIfExist(userId);
        Comment comment = getCommentIfExist(commId);
        if (!userId.equals(comment.getAuthor().getId()))
            throw new ConflictException("Only author can delete the comment");
        commentRepository.deleteById(commId);
        log.debug("Comment deleted");
    }

    @Override
    @Transactional(readOnly = true)
    public List<FullCommentDTO> getCommentsByAuthorId(Long userId, PageRequest pageRequest) {
        log.debug("Getting list of comments of user ID{}", userId);
        getUserIfExist(userId);
        List<Comment> comments = commentRepository.findAllByAuthorId(userId, pageRequest);
        return commentMapper.toFullCommentDTO(comments);
    }

    @Override
    @Transactional
    public void deleteCommentByAdmin(Long commId) {
        log.debug("Deleting comment ID{} by Admin", commId);
        getCommentIfExist(commId);
        commentRepository.deleteById(commId);
        log.debug("Comment deleted");
    }

    @Override
    @Transactional(readOnly = true)
    public FullCommentDTO getComment(Long commId) {
        log.debug("Getting comment ID{}", commId);
        Comment comment = getCommentIfExist(commId);
        return commentMapper.toFullCommentDTO(comment);
    }

    @Override
    @Transactional
    public List<ShortCommentDTO> getCommentsByEventId(Long eventId, PageRequest pageRequest) {
        log.debug("Getting list of comments of event ID{}", eventId);
        getEventIfExist(eventId);
        List<Comment> comments = commentRepository.findAllByEventId(eventId, pageRequest);
        return commentMapper.toShortCommentDTO(comments);
    }

    private User getUserIfExist(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format(USER_NOT_FOUND, userId)));
    }

    private Event getEventIfExist(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format(EVENT_NOT_FOUND, eventId)));
    }

    private Comment getCommentIfExist(Long commId) {
        return commentRepository.findById(commId).orElseThrow(() ->
                new NotFoundException(String.format(COMMENT_NOT_FOUND, commId)));

    }
}
