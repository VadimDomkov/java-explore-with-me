package ru.practicum.events.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.events.model.EventState;
import ru.practicum.users.dto.UserShortDto;

@Builder
@Data
public class EventShortDto {
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Integer views;
}
