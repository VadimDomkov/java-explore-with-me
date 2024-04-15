package ru.practicum.requests.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.requests.model.ParticipationRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    List<ParticipationRequest> findAllByEventIdAndEventInitiatorId(Long eventId, Long userId);

    List<ParticipationRequest> findAllByIdIn(List<Long> ids);

    boolean existsByEventIdAndRequesterId(Long eventId, Long userId);
}
