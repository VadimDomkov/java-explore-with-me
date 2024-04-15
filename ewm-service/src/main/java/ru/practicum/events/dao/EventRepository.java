package ru.practicum.events.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.events.model.Event;

import org.springframework.data.domain.Pageable;
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

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE (e.initiator.id IN ?1 OR ?1 IS null) "
            +
            "AND (e.state IN ?2 OR ?2 IS null) "
            +
            "AND (e.category.id IN ?3 OR ?3 IS null) "
            +
            "AND (e.eventDate > cast(?4 as date) OR cast(?4 as date) IS null) "
            +
            "AND (e.eventDate < cast(?5 as date) OR cast(?5 as date) IS null) "
    )
    List<Event> findAllByParameters(List<Long> ids, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);


    //Public Events
    @Query("SELECT e FROM Event e " +
            "WHERE ((?1 IS NULL) OR (upper(e.annotation) LIKE upper(concat('%', ?1, '%')) " +
            "OR upper(e.description) LIKE upper(concat('%', ?1, '%')))) " +
            "AND ((?2 IS NULL) OR (e.category.id IN ?2)) " +
            "AND ((?3 IS NULL) OR (e.paid = ?3)) " +
            "AND ((CAST(?4 AS date) IS NULL) OR (e.eventDate >= ?4)) " +
            "AND ((CAST(?5 AS date) IS NULL) OR (e.eventDate <= ?5)) " +
            "AND e.state = 'PUBLISHED'")
    List<Event> findEventsByUserParameters(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, Pageable pageable);
}
