package ru.practicum.events.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LocationDto {
    private double lat;

    private double lon;
}
