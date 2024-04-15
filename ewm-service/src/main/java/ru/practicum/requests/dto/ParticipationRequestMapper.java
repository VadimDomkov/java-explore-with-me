package ru.practicum.requests.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.requests.model.ParticipationRequest;

@Mapper(componentModel = "spring")
public interface ParticipationRequestMapper {

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    ParticipationRequestsDto requestToDto(ParticipationRequest request);

}
