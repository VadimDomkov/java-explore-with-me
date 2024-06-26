package ru.practicum.categories.service;

import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;

import java.util.List;

public interface CategoriesService {
    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto);

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Long catId);
}
