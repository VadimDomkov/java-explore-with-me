package ru.practicum.exceptions;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiError {
    private String message;
    private String reason;
    private String status;
    private LocalDateTime timestamp;
}
