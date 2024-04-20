package ru.practicum.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.dao.EventRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventState;
import ru.practicum.exceptions.EntityNotFoundException;
import ru.practicum.exceptions.ForbiddenException;
import ru.practicum.requests.dao.RequestRepository;
import ru.practicum.requests.dto.ParticipationRequestMapper;
import ru.practicum.requests.dto.ParticipationRequestsDto;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.model.RequestState;
import ru.practicum.users.dao.UsersRepository;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UsersRepository usersRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestMapper requestMapper;

    @Override
    public List<ParticipationRequestsDto> getUserParticipationRequests(Long userId) {
        return requestRepository.findAllByRequesterId(userId)
                .stream()
                .map(request -> requestMapper.requestToDto(request))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = false)
    public ParticipationRequestsDto createParticipationRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("События с id %d не найдено", eventId)));

        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с id %d не найдено", userId)));

        if (event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException("Нельзя отправлять запрос на участие в своём событии");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenException("Событие не опубликовано");
        }

        if ((event.getParticipantLimit() != 0L) && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ForbiddenException("Свободных мест нет");
        }

        if (requestRepository.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new ForbiddenException("Запрос уже был добавлен");
        }
        RequestState requestState;
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0L) {
            requestState = RequestState.CONFIRMED;
        } else {
            requestState = RequestState.PENDING;
        }

        ParticipationRequest participationRequest = ParticipationRequest.builder()
                .event(event)
                .requester(user)
                .status(requestState)
                .created(LocalDateTime.now())
                .build();

        if (requestState == RequestState.CONFIRMED) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        return requestMapper.requestToDto(requestRepository.save(participationRequest));
    }

    @Override
    @Transactional(readOnly = false)
    public ParticipationRequestsDto cancelParticipationRequest(Long userId, Long eventId) {
        usersRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с id %d не найдено", userId)));

        ParticipationRequest request = requestRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Запроса с id %d не найдено", eventId)));

        request.setStatus(RequestState.CANCELED);

        return requestMapper.requestToDto(requestRepository.save(request));
    }
}
