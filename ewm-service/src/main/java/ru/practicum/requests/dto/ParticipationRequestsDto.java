package ru.practicum.requests.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.requests.model.RequestState;

@Builder
@Data
public class ParticipationRequestsDto {
    private String created;
    private Long event;
    private Long id;
    private Long requester;
    private RequestState state;
}