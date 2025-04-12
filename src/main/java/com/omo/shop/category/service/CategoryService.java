package com.omo.shop.category.service;

import com.omo.shop.category.dto.CategoryDto;
import com.omo.shop.category.mapper.CategoryMapper;
import com.omo.shop.category.model.Category;
import com.omo.shop.category.repository.CategoryRepository;
import com.omo.shop.common.exceptions.AlreadyExistsException;
import com.omo.shop.common.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.omo.shop.common.constants.ExceptionMessages.CATEGORY_EXISTED;
import static com.omo.shop.common.constants.ExceptionMessages.CATEGORY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() ->
                        new ResourceNotFoundException(CATEGORY_NOT_FOUND));
        return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDtoList(categories);
    }

    @Override
    public CategoryDto addCategory(String categoryName) {
        if (categoryRepository.existsByName(categoryName)) {
            throw new AlreadyExistsException(categoryName + CATEGORY_EXISTED);
        }else{
            Category category = Category.builder()
                    .name(categoryName)
                    .build();
            Category savedCategory = categoryRepository.save(category);
            return categoryMapper.toDto(savedCategory);
        }
    }

    @Override
    public CategoryDto updateCategory(String newCategoryName, Long id) {
        // Check if the category exists by ID
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND));

        // If the name already exists (and it's not the current category), block the update
        if (categoryRepository.existsByName(newCategoryName) && category.getName().equals(newCategoryName)) {
            throw new AlreadyExistsException(CATEGORY_EXISTED);
        }

        // Update the name and save
        category.setName(newCategoryName);
        Category savedCategory = categoryRepository.save(category);

        // Map to DTO and return
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new ResourceNotFoundException(CATEGORY_NOT_FOUND);
                });
    }
}
