package ru.practicum.categories.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.categories.model.Category;

@Mapper(componentModel = "spring")
public interface CategoriesMapper {
    CategoryDto catToDto(Category category);

    Category newCatDtoToCat(NewCategoryDto dto);
}
