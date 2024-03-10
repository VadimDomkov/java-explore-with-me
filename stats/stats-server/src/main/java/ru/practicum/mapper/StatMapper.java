package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.StatRequestDto;
import ru.practicum.model.StatHit;

@Mapper(componentModel = "spring")
public interface StatMapper {
    StatHit requestDtoToHit(StatRequestDto dto);
}
