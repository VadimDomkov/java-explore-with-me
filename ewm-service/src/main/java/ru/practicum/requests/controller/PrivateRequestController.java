package ru.practicum.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.ParticipationRequestsDto;
import ru.practicum.requests.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Validated
@Slf4j
public class PrivateRequestController {
    private final RequestService requestService;

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestsDto createRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("POST to /users/{userId}/requests", userId);
        return requestService.createParticipationRequest(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestsDto> getRequests(@PathVariable Long userId) {
        log.info("GET to /users/{userId}/requests", userId);
        return requestService.getUserParticipationRequests(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestsDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("PATCH to /users/{userId}/requests/{requestId}/cancel", userId, requestId);
        return requestService.cancelParticipationRequest(userId, requestId);
    }

}
