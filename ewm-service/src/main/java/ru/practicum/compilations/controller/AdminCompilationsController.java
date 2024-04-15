package ru.practicum.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationDto;
import ru.practicum.compilations.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
@Slf4j
@Validated
public class AdminCompilationsController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("POST to /admin/compilations");
        return compilationService.createCompilation(newCompilationDto);
    }

    @DeleteMapping(path = "/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("DELETE to /admin/compilations/{compId}");
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping(path = "/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId, @Valid @RequestBody UpdateCompilationDto updateCompilationDto) {
        log.info("PATCH to /admin/compilations/{compId}");
        return compilationService.updateCompilation(compId, updateCompilationDto);
    }
}

