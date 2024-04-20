package ru.practicum.events.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.events.model.Event;
import ru.practicum.users.dto.UserShortDto;
import ru.practicum.users.model.User;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(source = "initiator", target = "initiator", qualifiedBy = UserToShortDtoMapper.class)
    EventShortDto eventToShortDto(Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "compilations", ignore = true)
    Event newDtoToEvent(NewEventDto newEventDto);

    @Mapping(source = "initiator", target = "initiator", qualifiedBy = UserToShortDtoMapper.class)
    EventFullDto eventToFullDto(Event event);

    @UserToShortDtoMapper
    public static UserShortDto userToShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
