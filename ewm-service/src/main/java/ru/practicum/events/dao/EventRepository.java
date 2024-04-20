package ru.practicum.events.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByIdIn(List<Long> list);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    boolean existsByCategoryId(Long id);

    //Admin Events
    List<Event> findAllByInitiatorId(Pageable pageable, Long userId);

    @Query("SELECT e FROM Event e " +
            "WHERE ((:ids IS NULL) OR (e.initiator.id IN :ids)) " +
            "AND ((:states IS NULL) OR (e.state IN :states)) " +
            "AND ((:categories IS NULL) OR (e.category.id IN :categories)) " +
            "AND ((CAST(:rangeStart AS date) IS NULL) OR (e.eventDate >= :rangeStart)) " +
            "AND ((CAST(:rangeEnd AS date) IS NULL) OR (e.eventDate <= :rangeEnd)) ")
    List<Event> findAllByParameters(List<Long> ids, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);


    //Public Events
    @Query("SELECT e FROM Event e " +
            "WHERE ((:text IS NULL) OR (upper(e.annotation) LIKE upper(concat('%', :text, '%')) " +
            "OR upper(e.description) LIKE upper(concat('%', :text, '%')))) " +
            "AND ((:categories IS NULL) OR (e.category.id IN :categories)) " +
            "AND ((:paid IS NULL) OR (e.paid = :paid)) " +
            "AND ((CAST(:rangeStart AS date) IS NULL) OR (e.eventDate >= :rangeStart)) " +
            "AND ((CAST(:rangeEnd AS date) IS NULL) OR (e.eventDate <= :rangeEnd)) " +
            "AND e.state = 'PUBLISHED'")
    List<Event> findEventsByUserParameters(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, Pageable pageable);
}
