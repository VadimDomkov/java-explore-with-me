package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotNull
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @NotNull
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @JsonProperty("eventDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private LocationDto location;
    private boolean paid;
    @PositiveOrZero
    private int participantLimit;
    private boolean requestModeration = true;
    @Size(min = 3, max = 120)
    private String title;
}
