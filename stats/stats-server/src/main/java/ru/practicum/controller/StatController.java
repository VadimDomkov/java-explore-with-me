package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatRequestDto;
import ru.practicum.StatResponseDto;
import ru.practicum.service.StatService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatController {

    private final StatService service;

    @PostMapping("/hit")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addStatHit(@RequestBody StatRequestDto requestDto) {
        log.info("Post request to /hit");
        service.addStatHit(requestDto);
    }

    @GetMapping("/stats")
    public List<StatResponseDto> getStats(@RequestParam String start,
                                          @RequestParam String end,
                                          @RequestParam(required = false) List<String> uris,
                                          @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("GET request to /stats");
        LocalDateTime startTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endTime = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return service.getStat(startTime, endTime, uris, unique);
    }

}
