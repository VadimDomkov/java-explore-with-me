package ru.practicum.comments.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.comments.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "event.title", target = "eventTitle")
    @Mapping(source = "author.name", target = "userName")
    CommentResponseDto commentToResponseDto(Comment comment);
}
