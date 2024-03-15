package ru.practicum.users.service;

import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;

import java.util.List;

public interface UserService {
    public UserDto createUser(NewUserRequest newUserRequest);

    public void deleteUser(Long userId);

    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);
}
