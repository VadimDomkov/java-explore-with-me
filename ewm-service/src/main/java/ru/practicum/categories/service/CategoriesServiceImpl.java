package ru.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.dao.CategoriesRepository;
import ru.practicum.categories.dto.CategoriesMapper;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.model.Category;
import ru.practicum.events.dao.EventRepository;
import ru.practicum.exceptions.EntityNotFoundException;
import ru.practicum.exceptions.ForbiddenException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CategoriesServiceImpl implements CategoriesService {
    private final CategoriesRepository categoriesRepository;
    private final CategoriesMapper categoriesMapper;
    private final EventRepository eventRepository;

    @Transactional(readOnly = false)
    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        return categoriesMapper.catToDto(
                categoriesRepository.save(
                        categoriesMapper.newCatDtoToCat(newCategoryDto)
                )
        );
    }

    @Transactional(readOnly = false)
    @Override
    public void deleteCategory(Long catId) {
        checkCategory(catId);
        if (eventRepository.existsByCategoryId(catId)) {
            throw new ForbiddenException("У категории есть связанные события");
        }
        categoriesRepository.deleteById(catId);
    }

    @Transactional(readOnly = false)
    @Override
    public CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto) {
        Category category = checkCategory(catId);

        category.setName(newCategoryDto.getName());
        return categoriesMapper.catToDto(categoriesRepository.save(category));
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return categoriesRepository.findAll(pageable).stream()
                .map(el -> categoriesMapper.catToDto(el))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        return categoriesMapper.catToDto(checkCategory(catId));
    }

    private Category checkCategory(Long catId) {
        return categoriesRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Категории с id %d не найдено", catId)));
    }
}
