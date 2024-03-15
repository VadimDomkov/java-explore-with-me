package ru.practicum.events.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.categories.dto.CategoryDto;

@Builder
@Data
public class NewEventDto {
    private String annotation;
    private CategoryDto category;
    private String description;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
}
