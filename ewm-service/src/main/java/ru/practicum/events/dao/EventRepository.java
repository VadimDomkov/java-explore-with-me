package ru.practicum.events.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.events.model.Event;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByIdIn(List<Long> list);

    List<Event> findAllByInitiator(Pageable pageable, Long userId);
}
