package ru.practicum.events.service;

import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventDto;
import ru.practicum.events.model.EventState;
import ru.practicum.requests.dto.ParticipationRequestUpdateDto;
import ru.practicum.requests.dto.ParticipationRequestsDto;
import ru.practicum.requests.dto.ParticipationStatusDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    //Private
    List<EventShortDto> getUserEvents(Long userId, int from, int size);

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getUserEventById(Long userId, Long eventId);

    EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventDto dto);

    List<ParticipationRequestsDto> getRequestsToUserEvents(Long userId, Long eventId);

    ParticipationStatusDto updateRequestState(Long userId, Long eventId, ParticipationRequestUpdateDto dto);

    //Admin
    List<EventFullDto> getAllEvents(List<Long> ids, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto updateEvent(Long eventId, UpdateEventDto dto);

    //Public
    List<EventShortDto> getPublicEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest httpServletRequest);

    EventFullDto getEventById(Long id, HttpServletRequest httpServletRequest);
}
