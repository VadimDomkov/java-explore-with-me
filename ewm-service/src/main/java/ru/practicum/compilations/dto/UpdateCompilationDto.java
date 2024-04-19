package ru.practicum.compilations.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationDto {
    private List<Long> events;
    private boolean pinned;
    @Length(max = 50)
    private String title;
}
