package ru.practicum.events.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class LocationDto {
    private double lat;

    private double lon;
}
