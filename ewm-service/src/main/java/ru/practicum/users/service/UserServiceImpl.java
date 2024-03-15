package ru.practicum.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.EntityNotFoundException;
import ru.practicum.users.dao.UsersRepository;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.UserMapper;
import ru.practicum.users.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {
    private final UsersRepository usersRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = false)
    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        return userMapper.userToDto(
                usersRepository.save(
                        userMapper.dtoToUser(newUserRequest)
                ));
    }

    @Transactional(readOnly = false)
    @Override
    public void deleteUser(Long userId) {
        usersRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с id %d не найдено", userId)));
        usersRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<User> result = ids == null ? usersRepository.findAll(pageable).toList() : usersRepository.findAllByIdIn(pageable, ids);
        return result.stream().map(el -> userMapper.userToDto(el)).collect(Collectors.toList());
    }
}
