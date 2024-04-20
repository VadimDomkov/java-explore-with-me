package ru.practicum.requests.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipationStatusDto {
    private List<ParticipationRequestsDto> confirmedRequests;

    private List<ParticipationRequestsDto> rejectedRequests;
}
