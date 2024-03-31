package ru.practicum.users.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDto {
    private Long id;

    private String email;

    private String name;
}