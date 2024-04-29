package ru.practicum.comments.service;


import ru.practicum.comments.dto.CommentRequestDto;
import ru.practicum.comments.dto.CommentResponseDto;
import ru.practicum.comments.model.Evaluation;

import java.util.List;

public interface CommentService {
    CommentResponseDto addComment(CommentRequestDto requestDto, Long eventId, Long userId, Evaluation evaluation);

    void deleteComment(Long commentId, Long userId);

    CommentResponseDto updateComment(CommentRequestDto requestDto, Long commentId, Long userId, Evaluation evaluation);

    CommentResponseDto getCommentById(Long commentId);

    List<CommentResponseDto> getUserComments(Long userId, Evaluation evaluation, Integer from, Integer size);

    List<CommentResponseDto> getEventComments(Long eventId, Evaluation evaluation, Integer from, Integer size);

}
