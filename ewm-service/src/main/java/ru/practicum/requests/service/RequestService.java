package ru.practicum.requests.service;

import ru.practicum.requests.dto.ParticipationRequestsDto;

import java.util.List;

public interface RequestService {
    //Users
    List<ParticipationRequestsDto> getUserParticipationRequests(Long userId);

    ParticipationRequestsDto createParticipationRequest(Long userId, Long eventId);

    ParticipationRequestsDto cancelParticipationRequest(Long userId, Long eventId);
}
