package ru.practicum.events.dto;

import org.mapstruct.Mapper;
import ru.practicum.events.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    Location dtoToLocation(LocationDto dto);
}
