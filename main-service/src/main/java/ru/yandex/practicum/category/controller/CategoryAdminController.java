package ru.yandex.practicum.category.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.category.dto.CategoryDTO;
import ru.yandex.practicum.category.dto.NewCategoryDTO;
import ru.yandex.practicum.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static ru.yandex.practicum.util.Constants.CATEGORIES_ADMIN_URI;
import static ru.yandex.practicum.util.Constants.USERS_ADMIN_URI;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(CATEGORIES_ADMIN_URI)
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO addCategory(@RequestBody @Valid NewCategoryDTO newCategoryDTO) {
        log.info("Response from POST request on {}", CATEGORIES_ADMIN_URI);
        return categoryService.addCategory(newCategoryDTO);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @Positive Long catId) {
        log.info("Response from DELETE request on {}/{}", CATEGORIES_ADMIN_URI, catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDTO updateCategory(@RequestBody @Valid NewCategoryDTO newCategoryDTO,
                                      @PathVariable @Positive Long catId) {
        log.info("Response from PATCH request on {}", USERS_ADMIN_URI);
        return categoryService.updateCategory(catId, newCategoryDTO);
    }
}