package ru.yandex.practicum.category.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.category.dto.CategoryDTO;
import ru.yandex.practicum.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.yandex.practicum.util.Constants.CATEGORIES_PUBLIC_URI;
import static ru.yandex.practicum.util.Utilities.fromSizePage;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(CATEGORIES_PUBLIC_URI)
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping("/{catId}")
    public CategoryDTO getCategory(@PathVariable @Positive Long catId) {
        log.info("Response from GET request on {}/{}", CATEGORIES_PUBLIC_URI, catId);
        return categoryService.getCategory(catId);
    }

    @GetMapping
    public List<CategoryDTO> getAllCategories(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                              @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Response from GET request on {}", CATEGORIES_PUBLIC_URI);
        return categoryService.getAllCategories(fromSizePage(from, size));
    }
}