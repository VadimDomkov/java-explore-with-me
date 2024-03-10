package ru.practicum.service;

import ru.practicum.StatRequestDto;
import ru.practicum.StatResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    void addStatHit(StatRequestDto requestDto);

    List<StatResponseDto> getStat(LocalDateTime start,
                                  LocalDateTime end,
                                  List<String> uris,
                                  Boolean unique);
}