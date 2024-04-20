package ru.practicum.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.requests.model.RequestState;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestUpdateDto {
    private List<Long> requestIds;

    private RequestState status;
}
