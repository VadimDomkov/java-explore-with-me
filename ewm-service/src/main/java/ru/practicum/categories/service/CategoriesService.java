package ru.practicum.categories.service;

import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;

import java.util.List;

public interface CategoriesService {
    public CategoryDto createCategory(NewCategoryDto newCategoryDto);

    public void deleteCategory(Long catId);

    public CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto);

    public List<CategoryDto> getCategories(Integer from, Integer size);

    public CategoryDto getCategoryById(Long catId);
}
