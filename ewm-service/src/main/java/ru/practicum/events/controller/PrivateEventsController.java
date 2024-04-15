package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventDto;
import ru.practicum.events.service.EventService;
import ru.practicum.requests.dto.ParticipationRequestUpdateDto;
import ru.practicum.requests.dto.ParticipationRequestsDto;
import ru.practicum.requests.dto.ParticipationStatusDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
@Validated
public class PrivateEventsController {
    private final EventService eventService;

    @GetMapping(path = "/{userId}/events")
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        log.info("GET to /users/{}/events", userId);
        return eventService.getUserEvents(userId, from, size);
    }

    @PostMapping(path = "/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId, @RequestBody @Valid NewEventDto newEventDto) {
        log.info("POST to /users/{}/events", userId);
        return eventService.createEvent(userId, newEventDto);
    }

    @GetMapping(path = "/{userId}/events/{eventId}")
    EventFullDto getUserEventById(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("GET to /users/{}/events/{}", userId, eventId);
        return eventService.getUserEventById(userId, eventId);
    }

    @PatchMapping(path = "/{userId}/events/{eventId}")
    public EventFullDto updateUserEvent(@PathVariable Long userId, @PathVariable Long eventId, @RequestBody @Valid UpdateEventDto dto) {
        log.info("PATCH to /users/{}/events/{}", userId, eventId);
        return eventService.updateUserEvent(userId, eventId, dto);
    }

    @GetMapping(path = "/{userId}/events/{eventId}/requests")
    List<ParticipationRequestsDto> getRequestsToUserEvents(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("GET to /users/{}/events/{}/requests", userId, eventId);
        return eventService.getRequestsToUserEvents(userId, eventId);
    }

    @PatchMapping(path = "/{userId}/events/{eventId}/requests")
    public ParticipationStatusDto updateRequestState(@PathVariable Long userId, @PathVariable Long eventId, @RequestBody @Valid ParticipationRequestUpdateDto dto) {
        log.info("PATCH to /users/{}/events/{}/requests", userId, eventId);
        return eventService.updateRequestState(userId, eventId, dto);
    }
}
