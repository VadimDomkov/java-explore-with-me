package ru.practicum.compilations.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.compilations.model.Compilation;

@Mapper(componentModel = "spring")
public interface CompilationMapper {
    CompilationDto compilationToDto(Compilation compilation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    Compilation dtoToComp(NewCompilationDto dto);
}
