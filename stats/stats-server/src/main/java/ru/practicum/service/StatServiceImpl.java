package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatRequestDto;
import ru.practicum.StatResponseDto;
import ru.practicum.mapper.StatMapper;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class StatServiceImpl implements StatService {
    private final StatMapper mapper;
    private final StatRepository repository;

    @Transactional(readOnly = false)
    @Override
    public void addStatHit(StatRequestDto requestDto) {
        repository.save(mapper.requestDtoToHit(requestDto));
    }

    @Override
    public List<StatResponseDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (uris == null) {
            log.info("uris is null");
            return unique ? repository.getAllUniqueStats(start, end) : repository.getAllStats(start, end);
        }
        return unique ? repository.getUniqueStatsByUris(start, end, uris) : repository.getStatsByUris(start, end, uris);

    }
}