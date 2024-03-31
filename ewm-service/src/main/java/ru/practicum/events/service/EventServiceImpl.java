package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.dao.CategoriesRepository;
import ru.practicum.categories.dto.CategoriesMapper;
import ru.practicum.events.dao.EventRepository;
import ru.practicum.events.dao.LocationRepository;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventState;
import ru.practicum.events.model.StateAction;
import ru.practicum.exceptions.EntityNotFoundException;
import ru.practicum.exceptions.ForbiddenException;
import ru.practicum.users.dao.UsersRepository;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
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
    private final CategoriesMapper categoriesMapper;

    @Override
    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);

        List<Event> events = eventRepository.findAllByInitiator(pageable, userId);
        return eventRepository.findAllByInitiator(pageable, userId)
                .stream()
                .map(el -> eventMapper.eventToShortDto(el))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = false)
    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
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
        if (dto.getAnnotation()!= null) {
            event.setAnnotation(dto.getAnnotation());
        }
        if(dto.getCategory()!=null) {
            event.setCategory(
                    categoriesRepository.findById(dto.getCategory().getId())
            .orElseThrow(() -> new EntityNotFoundException(String.format("Категории с id %d не найдено", dto.getCategory().getId()))));
        }
        if (dto.getDescription()!=null) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getEventDate()!=null) {
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getLocation()!=null) {
            event.setLocation(
                    locationRepository.findLocationByLatAndLon(
                            dto.getLocation().getLat(),
                            dto.getLocation().getLon()
                    ).orElseThrow(() -> new EntityNotFoundException("Локация не найдена")
            ));
        }
        if (dto.getPaid()!=null) {
            event.setPaid(dto.getPaid());
        }
        if (dto.getParticipantLimit()!=null) {
            event.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getRequestModeration()!=null) {
            event.setRequestModeration(dto.getRequestModeration());
        }
        if (dto.getStateAction()!=null) {
            event.setState(
                    dto.getStateAction() == StateAction.CANCEL_REVIEW ? EventState.CANCELED : EventState.PENDING
            );
        }
        if (dto.getTitle()!=null) {
            event.setTitle(dto.getTitle());
        }
        event.setPublishedOn(LocalDateTime.now());
        return eventMapper.eventToFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> getAllEvents(List<Long> ids, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {

        return null;
    }

    @Transactional(readOnly = false)
    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventDto dto) {
        return null;
    }

    @Override
    public List<EventShortDto> getPublicEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size) {
        return null;
    }

    @Override
    public EventFullDto getEventById(Long id) {
        return eventMapper.eventToFullDto(checkEvent(id));
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
