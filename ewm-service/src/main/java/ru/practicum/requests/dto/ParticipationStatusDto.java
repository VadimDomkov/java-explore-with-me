package ru.practicum.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationStatusDto {
    private List<ParticipationRequestsDto> confirmedRequests;

    private List<ParticipationRequestsDto> rejectedRequests;
}
