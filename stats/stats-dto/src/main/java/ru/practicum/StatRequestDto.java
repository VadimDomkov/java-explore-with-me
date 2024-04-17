package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatRequestDto {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
