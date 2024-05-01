package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentRequestDto;
import ru.practicum.comments.dto.CommentResponseDto;
import ru.practicum.comments.model.Evaluation;
import ru.practicum.comments.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments")
@Slf4j
@Validated
public class CommentController {
    private final CommentService commentService;

    @PostMapping(path = "/events/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto addComment(@Valid @RequestBody CommentRequestDto requestDto,
                                         @PathVariable Long eventId,
                                         @RequestParam Long userId,
                                         @RequestParam Evaluation evaluation) {
        log.info("POST to /comments/events/{}?userId={}&evaluation={}", eventId, userId, evaluation);
        return commentService.addComment(requestDto, eventId, userId, evaluation);
    }

    @DeleteMapping(path = "/{commentId}")
    void deleteComment(@PathVariable Long commentId,
                       @RequestParam Long userId) {
        log.info("DELETE to /comments/{}?userId={}", commentId, userId);
        commentService.deleteComment(commentId, userId);
    }

    @PatchMapping(path = "/{commentId}")
    CommentResponseDto updateComment(@Valid @RequestBody CommentRequestDto requestDto,
                                     @PathVariable Long commentId,
                                     @RequestParam Long userId,
                                     @RequestParam(required = false) Evaluation evaluation) {
        log.info("PATCH to /comments/{}?userId={}&evaluation={}", commentId, userId, evaluation);
        return commentService.updateComment(requestDto, commentId, userId, evaluation);
    }

    @GetMapping(path = "/{commentId}")
    CommentResponseDto getCommentById(@PathVariable Long commentId) {
        log.info("GET to /comments/{}", commentId);
        return commentService.getCommentById(commentId);
    }

    @GetMapping(path = "/users/{userId}")
    List<CommentResponseDto> getUserComments(@PathVariable Long userId,
                                             @RequestParam(required = false) Evaluation evaluation,
                                             @Min(0) @RequestParam(defaultValue = "0") int from,
                                             @Min(1) @RequestParam(defaultValue = "10") int size) {
        log.info("GET to /comments/users/{}?evaluation={}&from={}&size={}", userId, evaluation, from, size);
        return commentService.getUserComments(userId, evaluation, from, size);
    }

    @GetMapping(path = "/events/{eventId}")
    List<CommentResponseDto> getEventComments(@PathVariable Long eventId,
                                              @RequestParam(required = false) Evaluation evaluation,
                                              @Min(0) @RequestParam(defaultValue = "0") int from,
                                              @Min(1) @RequestParam(defaultValue = "10") int size) {
        log.info("GET to /comments/events/{}?evaluation={}&from={}&size={}", eventId, evaluation, from, size);
        return commentService.getEventComments(eventId, evaluation, from, size);
    }

}
