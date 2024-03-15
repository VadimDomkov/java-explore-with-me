package ru.practicum.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.service.CategoriesService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
@Validated
@Slf4j
public class AdminCategoriesController {
    private final CategoriesService categoriesService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("POST request to /admin/categories");
        return categoriesService.createCategory(newCategoryDto);
    }

    @DeleteMapping(path = "/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info("GET request to /admin/categories/{}", catId);
        categoriesService.deleteCategory(catId);
    }

    @PatchMapping(path = "/{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId,
                                      @Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("PATCH request to /admin/categories/{}", catId);
        return categoriesService.updateCategory(catId, newCategoryDto);
    }
}

