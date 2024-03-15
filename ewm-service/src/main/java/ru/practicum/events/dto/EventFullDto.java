package ru.practicum.events.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.events.model.EventState;
import ru.practicum.users.dto.UserShortDto;

@Builder
@Data
public class EventFullDto {
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Integer views;
}
