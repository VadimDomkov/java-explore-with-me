package ru.practicum.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.service.CategoriesService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
@Slf4j
@Validated
public class PublicCategoriesController {
    private final CategoriesService categoriesService;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        log.info("GET request to /categories");
        return categoriesService.getCategories(from, size);
    }

    @GetMapping(path = "/{catId}")
    public CategoryDto getCategoryBiId(@PathVariable Long catId) {
        log.info("GET request to /categories/{}", catId);
        return categoriesService.getCategoryById(catId);
    }
}
