package ru.yandex.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.category.dto.CategoryDTO;
import ru.yandex.practicum.category.dto.NewCategoryDTO;
import ru.yandex.practicum.category.mapper.CategoryMapper;
import ru.yandex.practicum.category.model.Category;
import ru.yandex.practicum.category.repository.CategoryRepository;
import ru.yandex.practicum.error.exception.NotFoundException;

import java.util.List;

import static ru.yandex.practicum.util.Constants.CATEGORY_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDTO addCategory(NewCategoryDTO newCategoryDTO) {
        log.info("Adding category {}", newCategoryDTO);
        Category category = categoryRepository.save(categoryMapper.toCategory(newCategoryDTO));
        log.info("Category is added {}", category);
        return categoryMapper.toCategoryDTO(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        log.info("Deleting category ID{}", catId);
        GetCategory(catId);
        categoryRepository.deleteById(catId);
        log.info("Category ID{} is deleted", catId);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long catId, NewCategoryDTO newCategoryDTO) {
        log.info("Updating category ID{}", catId);
        Category category = GetCategory(catId);
        category.setName(newCategoryDTO.getName());
        Category updatedCategory = categoryRepository.save(category);
        log.info("Category is updated {}", category);
        return categoryMapper.toCategoryDTO(updatedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategory(Long catId) {
        log.info("Getting category ID{}", catId);
        Category category = GetCategory(catId);
        return categoryMapper.toCategoryDTO(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories(PageRequest pageRequest) {
        log.info("Getting list of categories");
        return categoryMapper.toCategoryDTO(categoryRepository.findAll());
    }

    public Category GetCategory(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException(String.format(CATEGORY_NOT_FOUND, catId)));
    }
}