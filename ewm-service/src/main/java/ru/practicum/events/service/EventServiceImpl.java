package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatRequestDto;
import ru.practicum.StatsClient;
import ru.practicum.categories.dao.CategoriesRepository;
import ru.practicum.categories.dto.CategoriesMapper;
import ru.practicum.events.dao.EventRepository;
import ru.practicum.events.dao.LocationRepository;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventState;
import ru.practicum.events.model.StateAction;
import ru.practicum.exceptions.BadArgumentsException;
import ru.practicum.exceptions.EntityNotFoundException;
import ru.practicum.exceptions.ForbiddenException;
import ru.practicum.requests.dao.RequestRepository;
import ru.practicum.requests.dto.ParticipationRequestMapper;
import ru.practicum.requests.dto.ParticipationRequestUpdateDto;
import ru.practicum.requests.dto.ParticipationRequestsDto;
import ru.practicum.requests.dto.ParticipationStatusDto;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.model.RequestState;
import ru.practicum.users.dao.UsersRepository;
import ru.practicum.users.model.User;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoriesRepository categoriesRepository;
    private final UsersRepository usersRepository;
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final RequestRepository requestRepository;
    private final ParticipationRequestMapper requestMapper;
    private final StatsClient statsClient;

    @Override
    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);

        List<Event> events = eventRepository.findAllByInitiatorId(pageable, userId);
        return eventRepository.findAllByInitiatorId(pageable, userId)
                .stream()
                .map(el -> eventMapper.eventToShortDto(el))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = false)
    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadArgumentsException("Время события не может быть в прошлом");

        }
        Event event = eventMapper.newDtoToEvent(newEventDto);

        event.setCategory(
                categoriesRepository.findById(newEventDto.getCategory())
                        .orElseThrow(() -> new EntityNotFoundException(String.format("Категории с id %d не найдено", newEventDto.getCategory())))
        );

        event.setInitiator(
                usersRepository.findById(userId)
                        .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с id %d не найдено", userId)))
        );

        event.setLocation(
                locationRepository.save(locationMapper.dtoToLocation(newEventDto.getLocation()))
        );

        event.setCreatedOn(LocalDateTime.now());

        event.setState(EventState.PENDING);

        return eventMapper.eventToFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getUserEventById(Long userId, Long eventId) {
        User user = checkUser(userId);
        Event event = checkEvent(eventId);

        if (!event.getInitiator().getId().equals(userId)) {
            throw new EntityNotFoundException(String.format("У пользователя с %d не найдено не найдено события с id %d", userId, eventId));
        }
        return eventMapper.eventToFullDto(event);
    }

    @Transactional(readOnly = false)
    @Override
    public EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventDto dto) {
        User user = checkUser(userId);
        Event event = checkEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new EntityNotFoundException(String.format("У пользователя с %d не найдено не найдено события с id %d", userId, eventId));
        }
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenException("Only pending or canceled events can be changed");
        }
        if (dto.getAnnotation() != null) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getCategory() != null) {
            event.setCategory(
                    categoriesRepository.findById(dto.getCategory())
                            .orElseThrow(() -> new EntityNotFoundException(String.format("Категории с id %d не найдено", dto.getCategory()))));
        }
        if (dto.getDescription() != null) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getLocation() != null) {
            event.setLocation(
                    locationRepository.findLocationByLatAndLon(
                            dto.getLocation().getLat(),
                            dto.getLocation().getLon()
                    ).orElseThrow(() -> new EntityNotFoundException("Локация не найдена")
                    ));
        }
        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
        if (dto.getParticipantLimit() != null) {
            event.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        }
        if (dto.getStateAction() != null) {
            event.setState(
                    dto.getStateAction() == StateAction.CANCEL_REVIEW ? EventState.CANCELED : EventState.PENDING
            );
        }
        if (dto.getTitle() != null) {
            event.setTitle(dto.getTitle());
        }
        event.setPublishedOn(LocalDateTime.now());
        return eventMapper.eventToFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> getAllEvents(List<Long> ids, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {

        if ((rangeStart != null && rangeEnd != null) && rangeStart.isAfter(rangeEnd)) {
            throw new BadArgumentsException("Дата окончания должна быть позже даты начала");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> events;

        boolean isIds = ids != null ? true : false;
        boolean isStates = states != null ? true : false;
        boolean isCategories = categories != null ? true : false;

        events = eventRepository.findAllByParameters(ids, states, categories, rangeStart, rangeEnd, pageable);

        List<EventFullDto> dtos = events.stream()
                .map(event -> eventMapper.eventToFullDto(event))
                .collect(Collectors.toList());

        return dtos;
    }

    @Transactional(readOnly = false)
    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventDto dto) {
        Event event = checkEvent(eventId);

        if (dto.getEventDate() != null && dto.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new BadArgumentsException("Время события не может быть в прошлом");
        }

        if (dto.getAnnotation() != null) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getCategory() != null) {
            event.setCategory(
                    categoriesRepository.findById(dto.getCategory())
                            .orElseThrow(() -> new EntityNotFoundException(String.format("Категории с id %d не найдено", dto.getCategory()))));
        }
        if (dto.getDescription() != null) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getLocation() != null) {
            event.setLocation(
                    locationRepository.findLocationByLatAndLon(
                            dto.getLocation().getLat(),
                            dto.getLocation().getLon()
                    ).orElse(locationRepository.save(locationMapper.dtoToLocation(dto.getLocation()))));
        }
        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
        if (dto.getParticipantLimit() != null) {
            event.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        }
        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case REJECT_EVENT:
                    if (event.getState().equals(EventState.PUBLISHED)) {
                        throw new ForbiddenException("Можно отклонить только неопубликованные события");
                    }
                    event.setState(EventState.CANCELED);
                    break;
                case PUBLISH_EVENT:
                    if (!event.getState().equals(EventState.PENDING)) {
                        throw new ForbiddenException("Можно опубликовать только те события, которые ожидают публикации");
                    }
                    event.setState(EventState.PUBLISHED);
                    break;
            }
        }
        if (dto.getTitle() != null) {
            event.setTitle(dto.getTitle());
        }

        return eventMapper.eventToFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> getPublicEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest httpServletRequest) {
        if ((rangeStart != null && rangeEnd != null) && rangeStart.isAfter(rangeEnd)) {
            throw new BadArgumentsException("Дата окончания должна быть позже даты начала");
        }
        Pageable pageable;
        if (sort != null) {
            switch (sort) {
                case "EVENT_DATE":
                    pageable = PageRequest.of(from / size, size, Sort.by("eventDate"));
                    break;
                case "VIEWS":
                    pageable = PageRequest.of(from / size, size, Sort.by("views"));
                    break;
                default:
                    pageable = PageRequest.of(from / size, size);
            }
        } else {
            pageable = PageRequest.of(from / size, size);
        }
        List<Event> eventList = eventRepository.findEventsByUserParameters(text, categories, paid, rangeStart, rangeEnd, pageable);

        List<EventShortDto> dtos = eventList.stream()
                .map(event -> eventMapper.eventToShortDto(event))
                .collect(Collectors.toList());

        statsClient.addRequest(StatRequestDto.builder()
                .ip(httpServletRequest.getRemoteAddr())
                .app("ewm-service")
                .uri(httpServletRequest.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build());

        return dtos;
    }

    @Override
    public List<ParticipationRequestsDto> getRequestsToUserEvents(Long userId, Long eventId) {
        List<ParticipationRequest> requests = requestRepository.findAllByEventIdAndEventInitiatorId(eventId, userId);
        if (requests.isEmpty()) {
            return new ArrayList<>();
        }
        return requests.stream().map(
                request -> requestMapper.requestToDto(request)
        ).collect(Collectors.toList());
    }

    @Transactional(readOnly = false)
    @Override
    public ParticipationStatusDto updateRequestState(Long userId, Long eventId, ParticipationRequestUpdateDto dto) {
        checkUser(userId);

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("У пользователя с id %d не найдено события с id %d", userId, eventId)));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException("Можно подтверждать только запросы для своего события");
        }

        List<ParticipationRequest> requests = requestRepository.findAllByIdIn(dto.getRequestIds());

        if (requests.size() < dto.getRequestIds().size()) {
            throw new EntityNotFoundException("Список содержит некорректные идентификаторы запросов");
        }

        requests.forEach(request -> {
            if (!request.getStatus().equals(RequestState.PENDING)) {
                throw new ForbiddenException("Запрос должен быть в статусе PENDING");
            }
        });

        ParticipationStatusDto participationStatusDto = ParticipationStatusDto.builder()
                .confirmedRequests(new ArrayList<ParticipationRequestsDto>())
                .rejectedRequests(new ArrayList<ParticipationRequestsDto>())
                .build();

        if (dto.getStatus().equals(RequestState.REJECTED)) {
            requests.forEach(request -> {
                request.setStatus(RequestState.REJECTED);
                requestRepository.save(request);
                participationStatusDto.getRejectedRequests().add(requestMapper.requestToDto(request));
            });
        } else {
            if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                throw new ForbiddenException("Достигнут лимит одобренных заявок");
            }

            if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
                requests.forEach(request -> {
                    request.setStatus(RequestState.CONFIRMED);
                    requestRepository.save(request);
                    participationStatusDto.getConfirmedRequests().add(requestMapper.requestToDto(request));
                });
            } else {
                requests.forEach(
                        request -> {
                            if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                                request.setStatus(RequestState.CONFIRMED);
                                participationStatusDto.getConfirmedRequests().add(requestMapper.requestToDto(request));
                            } else {
                                request.setStatus(RequestState.REJECTED);
                                participationStatusDto.getRejectedRequests().add(requestMapper.requestToDto(request));
                            }
                            requestRepository.save(request);
                            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                        }
                );
            }
        }

        eventRepository.save(event);

        return participationStatusDto;
    }

    @Override
    public EventFullDto getEventById(Long id, HttpServletRequest httpServletRequest) {
        Event event = checkEvent(id);

        if (event.getState() != EventState.PUBLISHED) {
            throw new EntityNotFoundException(String.format("Событие с id %d не найдено", id));
        }

        statsClient.addRequest(StatRequestDto.builder()
                .ip(httpServletRequest.getRemoteAddr())
                .app("ewm-service")
                .uri(httpServletRequest.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build());

        event.setViews(event.getViews() + 1);
        return eventMapper.eventToFullDto(eventRepository.save(event));
    }

    private User checkUser(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с id %d не найдено", userId)));
    }

    private Event checkEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("События с id %d не найдено", eventId)));
    }
}
