package ru.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dao.CommentRepository;
import ru.practicum.comments.dto.CommentMapper;
import ru.practicum.comments.dto.CommentRequestDto;
import ru.practicum.comments.dto.CommentResponseDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.Evaluation;
import ru.practicum.events.dao.EventRepository;
import ru.practicum.events.model.Event;
import ru.practicum.exceptions.EntityNotFoundException;
import ru.practicum.users.dao.UsersRepository;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UsersRepository usersRepository;
    private final EventRepository eventRepository;
    private final CommentMapper commentMapper;


    @Override
    @Transactional(readOnly = false)
    public CommentResponseDto addComment(CommentRequestDto requestDto, Long eventId, Long userId, Evaluation evaluation) {

        Event event = checkEvent(eventId);

        User user = checkUser(userId);

        Comment comment = Comment.builder()
                .author(user)
                .event(event)
                .text(requestDto.getText())
                .evaluation(evaluation)
                .published(LocalDateTime.now())
                .build();

        return commentMapper.commentToResponseDto(commentRepository.save(comment));
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = checkComment(commentId);
        checkAuthor(comment.getAuthor().getId(), userId);
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional(readOnly = false)
    public CommentResponseDto updateComment(CommentRequestDto requestDto, Long commentId, Long userId, Evaluation evaluation) {
        Comment comment = checkComment(commentId);
        checkAuthor(comment.getAuthor().getId(), userId);
        comment.setText(requestDto.getText());
        if (evaluation != null) {
            comment.setEvaluation(evaluation);
        }

        return commentMapper.commentToResponseDto(commentRepository.save(comment));
    }

    @Override
    public CommentResponseDto getCommentById(Long commentId) {
        return commentMapper.commentToResponseDto(checkComment(commentId));
    }

    @Override
    public List<CommentResponseDto> getUserComments(Long userId, Evaluation evaluation, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Comment> list;
        if (evaluation != null) {
            list = commentRepository.findAllByAuthorIdAndEvaluation(userId, evaluation, pageable);
        } else {
            list = commentRepository.findAllByAuthorId(userId, pageable);
        }

        return list.stream().map(comment -> commentMapper.commentToResponseDto(comment)).collect(Collectors.toList());
    }

    @Override
    public List<CommentResponseDto> getEventComments(Long eventId, Evaluation evaluation, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Comment> list;
        if (evaluation != null) {
            list = commentRepository.findAllByEventIdAndEvaluation(eventId, evaluation, pageable);
        } else {
            list = commentRepository.findAllByEventId(eventId, pageable);
        }

        return list.stream().map(comment -> commentMapper.commentToResponseDto(comment)).collect(Collectors.toList());
    }

    private Comment checkComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Комментария с id %d не найдено", commentId)));
    }

    private User checkUser(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с id %d не найдено", userId)));
    }

    private void checkAuthor(long commentId, long authorId) {
        if (commentId != authorId) {
            throw new EntityNotFoundException(String.format("Комментарий с id %d не принадлежит пользователю %d", commentId, authorId));
        }
    }

    private Event checkEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("События с id %d не найдено", eventId)));
    }
}
