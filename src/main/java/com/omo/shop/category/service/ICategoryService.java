package com.omo.shop.category.service;

import com.omo.shop.category.dto.CategoryDto;
import com.omo.shop.category.model.Category;

import java.util.List;

public interface ICategoryService {
    CategoryDto getCategoryById(Long id);
    CategoryDto getCategoryByName(String name);
    List<CategoryDto> getAllCategories();
    CategoryDto addCategory(String category);
    CategoryDto updateCategory(Category category, Long id);
    void deleteCategory(Long id);
}
