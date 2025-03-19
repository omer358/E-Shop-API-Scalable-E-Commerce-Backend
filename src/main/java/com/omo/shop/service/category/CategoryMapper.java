package com.omo.shop.service.category;

import com.omo.shop.dto.CategoryDto;
import com.omo.shop.models.Category;

public class CategoryMapper {
    public static CategoryDto toDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public static Category toCategory(CategoryDto category) {
        return new Category(
                category.getName()
        );
    }
}
