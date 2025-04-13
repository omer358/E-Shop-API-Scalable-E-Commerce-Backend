package com.omo.shop.category.service;

import com.omo.shop.category.dto.CategoryDto;

import java.util.List;

public interface ICategoryService {
    CategoryDto getCategoryById(Long id);
    CategoryDto getCategoryByName(String name);
    List<CategoryDto> getAllCategories();
    CategoryDto addCategory(String category);
    CategoryDto updateCategory(String newCategoryName, Long id);
    void deleteCategory(Long id);
}
