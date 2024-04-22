package ru.yandex.practicum.category.service;

import org.springframework.data.domain.PageRequest;
import ru.yandex.practicum.category.dto.CategoryDTO;
import ru.yandex.practicum.category.dto.NewCategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO addCategory(NewCategoryDTO newCategoryDTO);

    void deleteCategory(Long catId);

    CategoryDTO updateCategory(Long catId, NewCategoryDTO newCategoryDTO);

    CategoryDTO getCategory(Long catId);

    List<CategoryDTO> getAllCategories(PageRequest pageRequest);
}