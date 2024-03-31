package ru.practicum.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.requests.model.RequestState;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestUpdateDto {
    private List<Long> requestIds;

    private RequestState status;
}
