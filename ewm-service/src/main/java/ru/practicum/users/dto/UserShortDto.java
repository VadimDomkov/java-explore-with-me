package ru.practicum.users.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class UserShortDto {
    private Long id;

    private String name;
}
