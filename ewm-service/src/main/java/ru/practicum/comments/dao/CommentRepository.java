package ru.practicum.comments.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.Evaluation;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByAuthorId(Long authorId, Pageable pageable);

    List<Comment> findAllByAuthorIdAndEvaluation(Long authorId, Evaluation evaluation, Pageable pageable);

    List<Comment> findAllByEventId(Long eventId, Pageable pageable);

    List<Comment> findAllByEventIdAndEvaluation(Long authorId, Evaluation evaluation, Pageable pageable);
}
