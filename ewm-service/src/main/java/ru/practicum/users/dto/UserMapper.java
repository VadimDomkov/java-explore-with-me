package ru.practicum.users.dto;

import org.mapstruct.Mapper;
import ru.practicum.users.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User dtoToUser(NewUserRequest newUserRequest);

    UserDto userToDto(User user);
}
