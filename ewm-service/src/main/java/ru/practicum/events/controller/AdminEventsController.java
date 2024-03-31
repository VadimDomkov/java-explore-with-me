package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.UpdateEventDto;
import ru.practicum.events.model.EventState;
import ru.practicum.events.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
@Slf4j
public class AdminEventsController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getAllEvents(@RequestParam(required = false) List<Long> ids,
                                           @RequestParam(name = "states", required = false) List<EventState> states,
                                           @RequestParam(name = "categories", required = false) List<Long> categories,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                           @Min(0) @RequestParam(defaultValue = "0") int from,
                                           @Min(1) @RequestParam(defaultValue = "10") int size) {

        log.info("GET to /admin/events");
        return eventService.getAllEvents(ids, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping(path = "/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId, @RequestBody @Valid UpdateEventDto dto) {
        log.info("PATCH to /admin/events/{}", eventId);
        return eventService.updateEvent(eventId, dto);
    }
}
