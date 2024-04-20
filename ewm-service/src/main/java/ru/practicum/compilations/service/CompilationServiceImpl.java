package ru.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dao.CompilationRepository;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.CompilationMapper;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.events.dao.EventRepository;
import ru.practicum.events.dto.EventMapper;
import ru.practicum.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Transactional(readOnly = false)
    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationMapper.dtoToComp(newCompilationDto);
        if (newCompilationDto.getEvents() != null) {
            compilation.setEvents(
                    eventRepository.findAllByIdIn(newCompilationDto.getEvents())
            );
        }

        return compilationMapper.compilationToDto(compilationRepository.save(compilation));
    }

    @Transactional(readOnly = false)
    @Override
    public void deleteCompilation(Long compId) {
        checkCompilation(compId);
        compilationRepository.deleteById(compId);
    }

    @Transactional(readOnly = false)
    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationDto newCompilationDto) {
        Compilation compilation = checkCompilation(compId);
        if (newCompilationDto.getEvents() != null) {
            compilation.setEvents(
                    eventRepository.findAllByIdIn(newCompilationDto.getEvents())
            );
        }
        if (newCompilationDto.getTitle() != null) {
            compilation.setTitle(newCompilationDto.getTitle());
        }
        compilation.setPinned(newCompilationDto.isPinned());

        return compilationMapper.compilationToDto(compilationRepository.save(compilation));
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Compilation> compilations = pinned == null ? compilationRepository.findAll(pageable).toList() : compilationRepository.getCompilationByPinned(pinned, pageable);
        return compilations.stream()
                .map(el -> compilationMapper.compilationToDto(el))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        return compilationMapper.compilationToDto(checkCompilation(compId));
    }

    private Compilation checkCompilation(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Подборки с id %d не найдено", compId)));
    }
}
