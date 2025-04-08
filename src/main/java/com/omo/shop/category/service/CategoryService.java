package com.omo.shop.category.service;

import com.omo.shop.category.dto.CategoryDto;
import com.omo.shop.category.mapper.CategoryMapper;
import com.omo.shop.category.model.Category;
import com.omo.shop.category.repository.CategoryRepository;
import com.omo.shop.exceptions.AlreadyExistsException;
import com.omo.shop.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "There is no category with the name"
                                        + name));
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
            throw new AlreadyExistsException(categoryName + " already exists ");
        }else{
            Category category = Category.builder()
                    .name(categoryName)
                    .build();
            Category savedCategory = categoryRepository.save(category);
            return categoryMapper.toDto(savedCategory);
        }
    }

    @Override
    public CategoryDto updateCategory(Category category, Long id) {
        //TODO: re-impl this method properly
//        Category updatedCategory = Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
//            oldCategory.setName(category.getName());
//            return categoryRepository.save(oldCategory);
//        }).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
//        return categoryMapper.toDto(updatedCategory);
        return null;
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new ResourceNotFoundException("Category not found");
                });
    }
}
