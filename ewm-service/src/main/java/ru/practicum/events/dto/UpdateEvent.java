package ru.practicum.events.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.events.model.StateAction;

@Builder
@Data
public class UpdateEvent {
    private String annotation;
    private CategoryDto category;
    private String description;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    private String title;
}
